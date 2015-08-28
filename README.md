# deface
这是一款类似美图秀秀的图片处理软件，添加了一些个性化的图片处理功能

## 项目主要构成

### 模块一：

调用系统相机进行拍照，并对图片进行处理；图片处理采用了调用本地方法类库的方式，类库是由旧版美图秀秀中提取出来的。案例中实现了四种方法的调用，具体调用方式请自己猜。。。

![image](https://github.com/wslqm123/deface/blob/master/screenshots/Screenshot_2015-08-28-00-43-40.png)
![image](https://github.com/wslqm123/deface/blob/master/screenshots/Screenshot_2015-08-28-00-43-59.png)

### 模块二：

浏览手机中照片，这里提供了有两种方式；1、调用系统自带图片浏览器，2、自己写代码查询手机中的照片；
案例中采用自己扫描照片的方式，默认选择图片最多的文件夹，使用recyleview对图片进行展示

![image](https://github.com/wslqm123/deface/blob/master/screenshots/Screenshot_2015-08-28-00-40-46.png)

### 模块三：

利用矩阵对bitmap进行变换，再与原来的bitmap进行合成，制造出镜像的效果，这里实现了垂直镜像和水平镜像两种效果

![image](https://github.com/wslqm123/deface/blob/master/screenshots/Screenshot_2015-08-28-00-44-49.png)
![image](https://github.com/wslqm123/deface/blob/master/screenshots/Screenshot_2015-08-28-00-45-01.png)

### 模块四：

利用两次拍摄的照片进行合成，制作出克隆的效果，相机preview和合成图片的显示比例还未调好，相机预览画面与实际合成有差别有待以后改善

其它模块还在开发中(图片保存功能在以后添加，目前只保存原始照片)，目前以找工作为主。。。

本项目中借鉴了一些无私开发者的代码（有一些代码直接copy，注释中作者也保留了下来，不知道这样是好还是不好。。），具体都是哪些实在不记得了，在这里表示感谢，项目中有任何不妥之处欢迎指出，日后有机会一定改善！
