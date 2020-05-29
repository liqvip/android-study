[TOC]

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

2. GestureDetector ，手势检测，用于辅助检测用户的单击、滑动、长按、双击等行为。使用步骤：
``` java
/*首先创建一个GestureDetector对象*/
GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
    ......
});
/*根据我们的需要，还可以实现OnDoubleTapListener从而能够监听双击行为*/
gestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
    ......
});
/*解决长按屏幕后无法拖动的情况*/
gestureDetector.setIsLongpressEnabled(false);
/*接着，在待监听View的onTouchEvent方法中添加如下实现*/
boolean consume = gestureDetector.onTouchEvent(event);
return consume;
```
<div align="center">OnGestureListener 和 OnDoubleTapListener 中方法的介绍<br>

| 方 法 名             | 描 述                                                                                                 | 所 属 接 口          |
|:---------------------|:------------------------------------------------------------------------------------------------------|:--------------------|
| onDown               | 手指轻轻触摸屏幕的一瞬间，由1个ACTION_DOWN触发                                                          | OnGestureListener   |
| onShowPress          | 手指轻轻触摸屏幕，尚未松开或拖动，由1个ACTION_DOWN触发                                                  | OnGestureListener   |
| onSingleTapUp        | 手指轻轻触摸屏幕后松开，伴随着一个MotionEvent ACTION_UP而触发，这是单击行为                              | OnGestureListener   |
| onScroll             | 手指按下屏幕并拖动，由一个ACTION_DOWN，多个ACTION_MOVE触发，这是拖动行为                                 | OnGestureListener   |
| onLongPress          | 用户长久的按着屏幕不放，即长按                                                                         | OnGestureListener   |
| onFling              | 用户按下触摸屏，快速滑动后松开，由一个 ACTION_DOWN、多个 ACTION_MOVE 和1个ACTION_UP触发，这是快速滑动行为 | OnGestureListener   |
| onSingleTapConfirmed | 严格的单击行为                                                                                        | OnDoubleTapListener |
| onDoubleTap          | 双击，由2次连续的单击组成，它不可能和onSingleTapConfirmed共存                                           | OnDoubleTapListener |
| onDoubleTapEvent     | 表示发生了双击行为，在双击的期间，ACTION_DOWN、ACTION_MOVE、和ACTION_UP都会触发此回调                    | OnDoubleTapListener |

</div>

3. Scroller，弹性滑动对象，用于实现 View 的弹性滑动。当使用View的scrollTo/scrollBy方法来进行滑动时，其过程是瞬间完成的，没有过渡效果。<br>
Scroller 本身无法让View弹性滑动，它需要和View的computeScroll方法配合使用才能共同完成这个功能。

#### 1.2 View 的滑动
##### 1.2.1 使用 scrollTo/scrollBy
scrollTo 实现了基于所传递参数的绝对滑动，scrollBy实现了基于当前位置的相对滑动。使用scrollTo和scrollBy来实现View的滑动，<br>
只能将View的内容进行滑动，并不能将View本身进行滑动。如果从左向右滑动，那么mScrollX为负值，反之为正值；如果从上往下滑动，<br>
那么mScrollY为负值，反之为正值。

##### 1.2.2 使用动画(View动画和属性动画)
**View 动画**是对 View 的影像做操作。它并不能真正改变 View 的位置参数，包括宽/高，并且如果希望动画后的状态得以保留还必须将fillAfter<br>
属性设置为true,否则动画完成后其动画结果会消失。View 动画还会带来一个很严重的问题，比如我们通过View动画将一个Button向右移动100px，<br>
并且这个View设置了单击事件，那么单击新位置将无法触发onClick事件，而单击原始位置仍然可以触发onClick事件。

从 Android 3.0 开始，使用**属性动画**可以解决上面的问题。但在**Android 2.2上无法使用属性动画**。
##### 1.2.3 改变布局参数
既改变LayoutParams

#### 1.3 弹性滑动
##### 1.3.1 使用 Scroller
当使用View的scrollTo/scrollBy方法来进行滑动时，其过程是瞬间完成的，没有过渡效果。这个时候就可以使用Scroller来实现有过度效果的滑动。
##### 1.3.2 通过动画
动画本身就是一种渐近的过程，因此通过它来实现的滑动天然就具有弹性效果。比如以下代码可以让一个View在100ms内向右移动100像素
``` java
ObjectAnimator.ofFloat(targetView,"translationX",0,100).setDuration(100).start();
```
##### 1.3.3 使用延时策略
核心思想是通过发送一系列延时消息从而达到一种渐进式效果，具体来说可以使用Handler或View的postDelayed方法，也可以使用线程的sleep方法。


#### 1.4 View 的事件分发机制
事件分发机制不仅仅是核心知识点更是难点。View 一大难点滑动冲突，解决方法的理论基础就是事件分发机制。
##### 1.4.1 点击事件的传递规则
点击事件的分析对象是MotionEvent。当一个MotionEvent产生了以后，系统需要把这个事件传递给一个具体的View，而这个传递的过程就是分发过程。点击事件的<br>
分发过程由3个很重要的方法共同完成：dispatchTouchEvent、onInterceptTouchEvent和onTouchEvent。
``` java
public boolean dispatchTouchEvent(Event ev){
    boolean consume = false;
    if(onInterceptTouchEvent(ev)){
        consume = onTouchEvent(ev);
    }else{
        consume = child.dispatchTouchEvent(ev);
    }
    return consume;
}
```
上述代码已经将三者的关系表现得淋漓尽致。
##### 1.4.2 事件分发的源码解析
当一个点击事件产生后，它的传递过程遵循如下顺序：Activity->Window->View。

**1.Activity 对点击事件的分发过程**
<div align="center">源码：Activity#dispatchTouchEvent</div>

``` java
public boolean dispatchTouchEvent(MotionEvent ev) {
    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
        onUserInteraction();
    }
    if (getWindow().superDispatchTouchEvent(ev)) {
        return true;
    }
    return onTouchEvent(ev);
}
```
首先事件交给Activity所附属的Window进行分发，如果返回true，整个事件就结束了。返回false意味着事件没人处理，
所有View的onTouchEvent都返回了false。那么Activity的onTouchEvent就会被调用。

**2.Window对点击事件的分发过程**
<div align="center">源码：PhoneWindow#superDispatchTouchEvent</div>

``` java
@Override
public boolean superDispatchTouchEvent(MotionEvent event) {
    return mDecor.superDispatchTouchEvent(event);
}
```





