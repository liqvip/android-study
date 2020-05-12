### View 的事件体系
#### 1.1 View 基础知识
##### 1.1.1 什么是 View
View 是 Android 中所有控件的基类，除了 View ，还有ViewGroup，即控件组

##### 1.1.2 View 的位置参数
&nbsp;&nbsp;&nbsp;&nbsp;View 的位置主要由它的四个顶点来决定，分别对应于View的四个属性：
1. top：左上角纵坐标，getTop()
2. left：左上角横坐标，getLeft()
3. right：右下角横坐标，getRight()
4. bottom：右下角纵坐标，getBottom()

这些坐标都是相对父View容器来说的，即它是一种相对坐标。  
&nbsp;&nbsp;&nbsp;&nbsp;从Android 3.0开始，View 增加了几个参数：
1. x，getX()
2. y，getY()
3. translationX，getTranslationX()
4. translationY，getTranslationY()

<div align="center">
换算关系：<br>
x = left + translatioinX<br>
y = top + translationY
</div>

##### 1.1.3 MotionEvent 和 TouchSlop
1. MotionEvent ，手指滑动事件<br>
在手指接触屏幕后所产生的一系列事件中，典型的事件类型有如下几种：
- ACTION_DOWN——手指刚接触屏幕
- ACTION_MOVE——手指在屏幕上滑动
- ACTION_UP——手指从屏幕上松开的一瞬间
<div align="center">
提供方法：<br>
getX()<br>
getY()<br>
getRawX()<br>
getRawY()<br>
</div>


2. TouchSlop 是系统所能识别出的被认为是滑动的最小距离<br>
`ViewConfiguration.get(getContext()).getScaledTouchSlop()`


##### 1.1.4 Velocity Tracker、GestureDetector 和 Scroller
1. Velocity Tracker ，速度追踪，用于追踪手指在滑动过程中的速度，包括水平和竖直方向的速度。<br>
``` java
/*首先，在View的onTouchEvent方法中追踪当前单击事件的速度：*/
VelocityTracker velocityTracker = VelocityTracker.obtain();
velocityTracker.addMovement(event);
/*接着，当我们想要知道当前的滑动速度时，可以采用如下方式获得当前的速度*/
velocityTracker.computeCurrentVelocity(1000);
int xVelocity = (int) velocityTracker.getXVelocity();
int yVelocity = (int) velocityTracker.getYVelocity();
/*最后，当不需要使用它的时候，需要调用clear方法来重置并回收内存*/
velocityTracker.clear();
velocityTracker.recycle();
```
