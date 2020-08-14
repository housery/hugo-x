package com.xiaohoo.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import com.xiaohoo.base.BaseEntity;

import javax.persistence.*;

/**
 * @author ：yangxiaohu
 * @date ：2020/7/9
 * @email :1126457667@qq.com
 */
@Entity
@Getter
@Setter
@Table(name="hugo_hot_search")
public class HotSearch extends BaseEntity {

    @Id
    @Column(name = "hot_id")
    @ApiModelProperty(value = "ID", hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "标签")
    private String tag;

    @ApiModelProperty(value = "排序")
    private Integer orders;

    @ApiModelProperty(value = "详情链接")
    @Column(length = 1000)
    private String url;

    @ApiModelProperty(value = "参与人数")
    @Column(name = "people_count")
    private Long peopleCount;

    @ApiModelProperty(value = "来源")
    private String source;
}
