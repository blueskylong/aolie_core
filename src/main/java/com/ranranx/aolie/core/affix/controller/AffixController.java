package com.ranranx.aolie.core.affix.controller;

import com.ranranx.aolie.core.affix.dto.AffixDto;
import com.ranranx.aolie.core.affix.service.AffixService;
import com.ranranx.aolie.core.common.SessionUtils;
import com.ranranx.aolie.core.handler.HandleResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

/**
 * 附件管理
 *
 * @author xxl
 * @version V0.0.1
 * @date 2021/3/17 0017 20:31
 **/

@RestController
public class AffixController {

    @Autowired
    AffixService affixService;

    /**
     * 部署流程
     */
    @PostMapping("/upload/{columnId}/{rowId}")
    public Object uploadFiles(MultipartHttpServletRequest request,
                              @PathVariable Long columnId,
                              @PathVariable Long rowId) throws IOException {
        return affixService.uploadFiles(request, columnId, rowId);
    }

    @GetMapping("/findAffixList/{columnId}/{bizId}")
    public HandleResult findAffixByBiz(@PathVariable Long columnId, @PathVariable Long bizId) {
        HandleResult result = HandleResult.success(2);
        result.setData(affixService.findAffixByBizId(columnId, bizId));
        return result;
    }

    @RequestMapping("/deleteAffix/{affixId}")
    public HandleResult deleteModular(@PathVariable Long affixId) {
        int result = affixService.deleteAffixInfo(affixId, SessionUtils.getLoginUser());
        return HandleResult.success(result);
    }

    @RequestMapping("/deleteAffixByBiz/{columnId}/{bizId}")
    public HandleResult deleteAffixByBiz(@PathVariable Long columnId, @PathVariable Long bizId) {
        return HandleResult.success(affixService.deleteAffixByBiz(columnId, bizId));
    }

    @GetMapping("/download/{affixId}")
    public ResponseEntity<byte[]> downloadAffix(@PathVariable long affixId) {
        return affixService.downloadAffix(affixId);
    }

    @GetMapping("/download/{columnId}/{bizId}")
    public ResponseEntity<byte[]> downloadAffixList(
            @PathVariable Long columnId, @PathVariable Long bizId) {
        return affixService.downloadAffixList(columnId, bizId);
    }


    @GetMapping("/getAffixById/{affixId}")
    public AffixDto getAffixById(@PathVariable Long affixId) {
        return affixService.getAffixById(affixId, SessionUtils.getLoginUser());
    }


}
