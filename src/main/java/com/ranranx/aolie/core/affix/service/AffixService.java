package com.ranranx.aolie.core.affix.service;

import com.ranranx.aolie.core.affix.dto.AffixDto;
import com.ranranx.aolie.core.runtime.LoginUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 附件服务
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/18 0018 17:15
 **/
public interface AffixService {
    /**
     * 根据id查询附件基本信息
     *
     * @param affixId
     * @param user
     * @return
     */
    AffixDto getAffixById(long affixId, LoginUser user);

    /**
     * 根据业务主键查询附件列表
     *
     * @param columnId
     * @param affixId
     * @return
     */
    List<AffixDto> findAffixByBizId(Long columnId, Long affixId);

    /**
     * 保存上传文件
     *
     * @param request
     * @param columnId
     * @param rowId
     * @return
     * @throws IOException
     */
    Object uploadFiles(MultipartHttpServletRequest request,
                       @PathVariable Long columnId,
                       @PathVariable Long rowId) throws IOException;


    /**
     * 删除附件,需要判断当前附件是否已经被删除了
     *
     * @param affixId
     * @return
     */
    int deleteAffixInfo(long affixId, LoginUser user);

    /**
     * 根据业务主键删除附件
     *
     * @param columnId
     * @param bizId
     * @return
     */
    int deleteAffixByBiz(Long columnId, Long bizId);

    /**
     * 根据附件的主键,进行附件的下载
     *
     * @param affixId
     */
    ResponseEntity<byte[]> downloadAffix(long affixId);

    /**
     * 根据附件的主键数组,进行附件的批量下载
     */
    ResponseEntity<byte[]> downloadAffixList(Long columnId, Long bizId);


    AffixDto findBaseAffix(long affixId);


    /**
     * 根据affixId获取对应附件的byte[]
     *
     * @param req
     * @param affixId
     * @return
     */
    byte[] getByteArray(HttpServletRequest req, Long affixId);


}
