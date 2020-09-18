package cn.blogss.android_study.home.bean;

/**
 * @创建人 560266
 * @文件描述 首页 RecyclerView 的 Item 项数据
 * @创建时间 2020/4/29
 */
public class HomeRvItemBean {
    /*item 名称*/
    private String name;

    /*item 的背景图*/
    private int itemIcon;

    /*跳转Activity的名称*/
    private String actName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(int itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }
}
