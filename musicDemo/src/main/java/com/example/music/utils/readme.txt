一、使用了3个Fragment。

    1、MusicInfoFragment：用来显示歌手名、歌曲名
    2、MusicListFragment：用来展示音乐列表信息
    3、MusicControlFragment：用来显示进度和控制音乐播放

二、使用了回调接口、Handler等实现Activity、Fragment、Service 等组件的通信。

    1、回调接口：提供信息的类提供接口（声明方法或常量），使用信息的类实例化接口的方法（用来处理信息）；
如：在MainActivity中提供歌手名、歌曲名（这些信息从服务里获得。也可以通过Handler机制从服务获得，下面有讲到），
然后在MainActivity中定义一个回调接口，在让需要这些信息的类（比如 Fragment ）实现这个接口。这样，
MainActivit就可以回调这个接口中的回调方法向 Fragment 中传递数据了。

     所谓的回调，就是你实现我的接口，我调用该接口的方法，即就是某一个类A提供回调接口，另一个类B实现该接口，然后A调用接口的方法（通过实现类B的引用）；
 这样B就可以立即响应并处理信息。

    2、Handler：暂不赘述。

三、不好的代码。
    1、不好的方式更新 MusicControlFragment ,新的方式是在Fragment里定义一个handler,在服务里发送消息到handler。

    public final static Handler mHander = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = (Bundle) msg.obj;//取得信息

                if (bundle != null) {
                    //通过回调接口方式更新 MusicInfoFragment
                    callbackMusicInfo.setInfo(bundle.getString("artist"),bundle.getString("title"));
                    //不好的方式,每次都要重新更新 Fragemnt ,新的方式是在Fragment里定义一个handler,在服务里发送消息到handler

                  musicControlFragment = new MusicControlFragment();
                  musicControlFragment.setArguments(bundle);
                  fragmentManager.beginTransaction().replace(R.id.music_control, musicControlFragment).commitAllowingStateLoss();
                }
            }
        };
    该方式中 ：fragmentManager.beginTransaction().replace(R.id.music_control, musicControlFragment).commitAllowingStateLoss();
每1秒更新一次MusicControlFragment。而且，此处先是使用了
fragmentManager.beginTransaction().replace(R.id.music_control, musicControlFragment).commit();
这种方式commit会做一个状态保存，每次切换会出现异常，所以采用commitAllowingStateLoss()。这种方式不做状态保存。

四、在服务 MusicPlayService 里用分别向 Mainactivity  和  MusicControlFragment同时使用 handler发送了两条不同的消息，
发到消息队列中，不能是同一条消息。消息队列不能保存两条相同的消息（暂称为“队列中消息的互异性”）。
   private void getCurrentMusicInfo(){
        if (cursor != null) {
            String artist = Tools.cursor.getString(Tools.cursor.getColumnIndexOrThrow(Tools.cursorCols[0]));//歌手
            String title = Tools.cursor.getString(Tools.cursor.getColumnIndexOrThrow(Tools.cursorCols[1]));//歌名
            int currentPosition = mPlayer.getCurrentPosition();
            int duration = Integer.valueOf(cursor.getString(cursor
                    .getColumnIndexOrThrow(cursorCols[3])));
            String nowTime = Tools.changeTime(currentPosition);
            String endTime = Tools.changeTime(duration);//歌名

            //通过handler传递musicInfo的相关信息
            Bundle bundle = new Bundle();
            Bundle bundle2 = new Bundle();
            Message msg = mHander.obtainMessage();
            Message msg2 = controlHandler.obtainMessage();

            bundle.putString("title", title);
            bundle.putString("artist", artist);

            bundle2.putString("nowTime", nowTime);
            bundle2.putString("endTime", endTime);

            bundle2.putInt("currentPosition",currentPosition);
            bundle2.putInt("duration",duration);

            msg.obj = bundle;
            msg2.obj = bundle2;
            mHander.sendMessage(msg);
            controlHandler.sendMessage(msg2);
        }
    }