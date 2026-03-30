package cn.blogss.kotlin

// 约束类
interface IGamePlayer {
    // 打排位赛
    fun rank()
    // 升级
    fun upgrade()
}

// 被委托对象，本场景中的游戏代练
class RealGamePlayer(private val name: String): IGamePlayer{
    override fun rank() {
        println("$name 开始排位赛")
    }

    override fun upgrade() {
        println("$name 升级了")
    }

}

// 委托对象
class DelegateGamePlayer(private val player: IGamePlayer): IGamePlayer by player

// Client 场景测试
fun main() {
    val realGamePlayer = RealGamePlayer("张三")
    val delegateGamePlayer = DelegateGamePlayer(realGamePlayer)
    delegateGamePlayer.rank()
    delegateGamePlayer.upgrade()
}
