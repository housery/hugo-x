/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package com.xiaohoo.baidurec.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://docs.auauz.net
* @description /
* @author yangxiaohu
* @date 2020-11-17
**/
@Data
public class BaiduSearchRecDto implements Serializable {

    /** ID */
    private Long id;

    /** 搜索关键词 */
    private String keyword;

    private String createBy;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String updateBy;

    private Long pid;
}