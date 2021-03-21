package com.ranranx.aolie.core.affix.service.impl;

import com.ranranx.aolie.core.affix.dto.AffixDto;
import com.ranranx.aolie.core.affix.service.AffixService;
import com.ranranx.aolie.core.common.CommonUtils;
import com.ranranx.aolie.core.common.Constants;
import com.ranranx.aolie.core.common.IdGenerator;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.exceptions.InvalidParamException;
import com.ranranx.aolie.core.handler.HandleResult;
import com.ranranx.aolie.core.handler.HandlerFactory;
import com.ranranx.aolie.core.handler.param.DeleteParam;
import com.ranranx.aolie.core.handler.param.InsertParam;
import com.ranranx.aolie.core.handler.param.QueryParam;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 附件服务
 * 这是一个独立的附件体系,
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/18 0018 17:16
 **/
@Service
@Transactional(readOnly = true)
public class AffixServiceImpl implements AffixService {

    private Logger logger = LoggerFactory.getLogger(AffixServiceImpl.class);
    @Value("${dm.uploadpath:upload}")
    private String UPLOAD_PATH;

    /**
     * 默认上传目录
     */
    private static String DEFAULT_UPLOAD_DIR = "upload";

    private static final DateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Autowired
    private HandlerFactory handlerFactory;


    /**
     * 文件上传,这里没有限制上传的数量,数据由前端控制
     *
     * @param request  请求数据
     * @param columnId 列信息(必须提供)
     * @param rowId    如果是新增加,这个值可以给个负数.
     * @return 返回上传的情况, 并会将ID返回
     * @throws IOException
     */
    @Override
    @Transactional(readOnly = false)
    public Object uploadFiles(MultipartHttpServletRequest request, Long columnId, Long rowId) throws IOException {
        Iterator<String> itr = request.getFileNames();
        AffixDto dto;
        if (itr == null && !itr.hasNext()) {
            throw new InvalidParamException("没有检测到文件！");
        }
        try {
            String path = genFullPath();

            File filePath = new File(path);
            if (!filePath.exists()) {
                filePath.mkdirs();
            }
            while (itr.hasNext()) {
                dto = new AffixDto();

                MultipartFile file = request.getFile(itr.next());
                String fileName = file.getOriginalFilename();

                logger.info(">>>>>>>>>>>>>>收到文件:" + fileName);
                dto.setRowId(rowId);
                dto.setColumnId(columnId);
                dto.setFileExt(fileName.substring(fileName.lastIndexOf(".") + 1));
                dto.setAffixId(-1L);
                dto.setOraFilename(fileName);
                dto.setFilePath(path);
                dto.setFileName(IdGenerator.getNextId(AffixDto.class.getName()) + "");
                //一次保存,只会有一个ROWID
                rowId = insertAffixInfo(dto);


                //保存文件
                String newFileName = path + File.separator + dto.getFileName();
                FileOutputStream outputStream = new FileOutputStream(newFileName);
                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            throw new InvalidParamException("上传出现异常！" + e.getMessage());
        }
        AffixResponse object = new AffixResponse();
        object.setId(rowId + "");
        return object;
    }

    /**
     * 取得保存的路径
     *
     * @return
     */
    private String genFullPath() {
        return getFixSavePath() + File.separator + genSubDir();
    }

    /**
     * 保存附件数据,可能会生成行ID
     *
     * @param affixInfo
     * @return
     */
    private long insertAffixInfo(AffixDto affixInfo) {
        InsertParam insertParam = new InsertParam();
        //如果是新增加的行数据,这里生成rowId
        if (affixInfo.getRowId() == null || affixInfo.getRowId() < 1) {
            affixInfo.setRowId(IdGenerator.getNextId(AffixDto.class.getName()));
        }
        insertParam.setObject(affixInfo, Constants.DEFAULT_DM_SCHEMA);
        HandleResult result = handlerFactory.handleInsert(insertParam);
        return affixInfo.getRowId();
    }

    private String getFixSavePath() {
        if (DEFAULT_UPLOAD_DIR.equals(UPLOAD_PATH) || CommonUtils.isEmpty(UPLOAD_PATH)) {
            UPLOAD_PATH = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                    .getSession().getServletContext().getRealPath(File.separator + DEFAULT_UPLOAD_DIR);
        }
        return UPLOAD_PATH;
    }

    @Override
    @Transactional(readOnly = false)
    public int deleteAffixByBiz(Long columnId, Long bizId) {
        List<AffixDto> lstDto = findAffixByBizId(columnId, bizId);
        if (lstDto == null || lstDto.isEmpty()) {
            return 0;
        }
        lstDto.forEach(dto -> deleteAffixInfo(dto));
        return lstDto.size();
    }

    /**
     * 生成分目录结构,当前使用 年月为分隔,也就是一个月使用一目录
     *
     * @return
     */
    private String genSubDir() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        return format.format(new Date());
    }

    @Override
    @Transactional(readOnly = false)
    public int deleteAffixInfo(long affixId, LoginUser user) {
        AffixDto dto = getAffixById(affixId, user);
        if (dto == null) {
            return 0;
        }
        //删除文件,
        //删除前,可以检查一下有没有其它的引用
        HandleResult result = deleteAffixInfo(dto);
        // 获取所有需要删除的数据


        File file = new File(getFixSavePath()
                + getSysFilePath(dto)
                + File.separator + dto.getFileName());
        if (file.exists()) {
            file.delete();
        }


        return 1;
    }

    private HandleResult deleteAffixInfo(AffixDto affixDto) {
        return handlerFactory.handleDelete(new DeleteParam().setDeleteDto(Constants.DEFAULT_DM_SCHEMA, affixDto));
    }

    private String getSysFilePath(AffixDto affix) {
        if (affix != null) {
            return getSysFilePath(affix.getFilePath());
        } else {
            return null;
        }
    }

    private String getSysFilePath(String filePath) {
        if (!StringUtils.hasLength(filePath)) {
            if (File.separator.equals("\\")) {
                return filePath.replaceAll("/", "\\\\");
            } else {
                return filePath.replaceAll("\\\\", File.separator);
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> downloadAffix(long affixId) {
        AffixDto affixInfo = findBaseAffix(affixId);
        if (affixInfo != null) {
            String filePath = affixInfo.getFilePath();
            String realFileName = affixInfo.getOraFilename();
            String path = getFixSavePath();
            File file = new File(path + getSysFilePath(filePath) + File.separator + realFileName);
            HttpHeaders headers = new HttpHeaders();
            String fileName;
            try {
                //为了解决中文名称乱码问题
                fileName = new String(affixInfo.getFileName().getBytes("gbk"), "iso-8859-1");
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    public byte[] getByteArray(HttpServletRequest req, Long affixId) {
        AffixDto affixQuery = new AffixDto();
        affixQuery.setAffixId(affixId);
        AffixDto affixInfo = getAffixById(affixId, SessionUtils.getLoginUser());
        if (affixInfo != null) {
            byte[] fileByte = null;
            String filePath = affixInfo.getFilePath();
            String realFileName = affixInfo.getFileName();
            String path = getFixSavePath();
            String fileRealPath = path + getSysFilePath(filePath) + File.separator + realFileName;
            File file = new File(fileRealPath);
            FileInputStream fileInputStream = null;
            ByteArrayOutputStream byteArrayOutStream = null;
            byte[] byteArray = new byte[1024];
            int n;
            try {
                fileInputStream = new FileInputStream(file);
                byteArrayOutStream = new ByteArrayOutputStream(1024);
                while ((n = fileInputStream.read(byteArray)) != -1) {
                    byteArrayOutStream.write(byteArray, 0, n);
                }
                fileByte = byteArrayOutStream.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileInputStream != null) {
                    try {
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (byteArrayOutStream != null) {
                    try {
                        byteArrayOutStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return fileByte;
        }
        return null;
    }


    private byte[] getByteByFile(File file) throws IOException {
        FileInputStream in2 = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in2.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        in2.close();
        byte[] result = out.toByteArray();
        out.close();
        return result;
    }

    @Override
    public ResponseEntity<byte[]> downloadAffixList(Long columnId, Long bizId) {
        List<AffixDto> lstDto = findAffixByBizId(columnId, bizId);
        List<Map<String, Object>> affixfilePath = new ArrayList<Map<String, Object>>();
        if (lstDto == null || lstDto.isEmpty()) {
            return null;
        }
        String path = getFixSavePath();
        for (AffixDto affix : lstDto) {
            Map<String, Object> map = new HashMap<String, Object>();
            String filePath = affix.getFilePath();
            String realFileName = affix.getOraFilename();

            map.put("filePath", path + getSysFilePath(filePath) + File.separator + realFileName);
            map.put("fileName", affix.getFileName());
            affixfilePath.add(map);
        }
        File file = null;
        try {
            String zipPath = FileTools.batchExportAffixFile(getFixSavePath(), affixfilePath);
            file = new File(zipPath);
            String zipName = "资源库附件导出文件.zip";
            HttpHeaders headers = new HttpHeaders();
            zipName = new String(zipName.getBytes("gbk"), "iso-8859-1");//为了解决中文名称乱码问题
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + zipName + "\"");
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
        return null;
    }


    @Override
    public AffixDto findBaseAffix(long affixId) {
        AffixDto affixDto = new AffixDto();
        affixDto.setAffixId(affixId);
        QueryParam queryParam = new QueryParam()
                .setFilterObjectAndTableAndResultType(Constants.DEFAULT_DM_SCHEMA, SessionUtils.getLoginVersion(), affixDto);
        return (AffixDto) handlerFactory.handleQuery(queryParam).singleValue();
    }

    /**
     * 根据业务主键查询附件列表
     *
     * @param columnId 列ID
     * @param rowID    附件业务键ID
     * @return
     */
    @Override
    public List<AffixDto> findAffixByBizId(Long columnId, Long rowID) {
        AffixDto dto = new AffixDto();
        dto.setAffixId(rowID);
        dto.setColumnId(columnId);
        QueryParam param = new QueryParam()
                .setFilterObjectAndTableAndResultType(Constants.DEFAULT_DM_SCHEMA,
                        SessionUtils.getLoginVersion(), dto);
        return handlerFactory.handleQuery(param).getLstData();
    }

    /* (non-Javadoc)
     * @see com.wenzheng.module.common.components.affix.service.AffixDtoService#getAffixById(java.lang.String, com.wenzheng.platform.core.bean.LoginUser)
     */
    @Override
    public AffixDto getAffixById(long affixId, LoginUser user) {
        AffixDto affix = new AffixDto();
        affix.setAffixId(affixId);
        affix.setVersionCode(user.getVersionCode());
        QueryParam param = new QueryParam();
        param.setTableDtoAndResultType(Constants.DEFAULT_DM_SCHEMA, user.getVersionCode(), AffixDto.class)
                .appendCriteria().andEqualToDto(null, affix);
        return (AffixDto) handlerFactory.handleQuery(param).singleValue();
    }

    static class AffixResponse implements Serializable {
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
