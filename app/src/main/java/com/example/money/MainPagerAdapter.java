package com.example.money;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList=new ArrayList<>();
    private List<String> fragmentTitle=new ArrayList<>();
    public MainPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    //計算要做出幾個分頁
    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }
    // 取得要新增的Fragment與你為它取名的title
    public void addFragment(Fragment fragment,String title){
        fragmentList.add(fragment);
        fragmentTitle.add(title);
    }
    // 設置ViewPager的標題，之後可以判斷目前位於哪個fragment
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}