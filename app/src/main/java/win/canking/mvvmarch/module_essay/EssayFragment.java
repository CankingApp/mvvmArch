package win.canking.mvvmarch.module_essay;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import win.canking.mvvmarch.R;
import win.canking.mvvmarch.architecture.Resource;
import win.canking.mvvmarch.module_essay.db.entity.ZhihuItemEntity;
import win.canking.mvvmarch.module_essay.db.entity.ZhihuStoriesEntity;
import win.canking.mvvmarch.module_essay.ui.EssayListAdapter;
import win.canking.mvvmarch.module_essay.viewmodel.EssayViewModel;

import static win.canking.mvvmarch.module_essay.ui.EssayListAdapter.MultiItem.TYPE_BASE;

/**
 * Created by changxing on 2017/12/3.
 */

public class EssayFragment extends Fragment {
    public static final String TAG = "EssayFragment";
    private RecyclerView mListView;
    private EssayListAdapter mAdapter;
    private SwipeRefreshLayout refreshLayout;
    private EssayViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.essay_fragment, null);
        mListView = v.findViewById(R.id.products_list);
        refreshLayout = v.findViewById(R.id.refresh);
        initView();
        subscribeUi();
        return v;
    }

    private void initView() {
        viewModel = ViewModelProviders.of(this).get(EssayViewModel.class);

        View footerView = new View(getActivity());
        footerView.setLayoutParams(new RecyclerView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                getResources().getDimensionPixelOffset(R.dimen.bottom_btn_bg_heigh)));

        mAdapter = new EssayListAdapter(getActivity(), new ArrayList<EssayListAdapter.MultiItem>());
        mAdapter.bindToRecyclerView(mListView);
        mAdapter.addFooterView(footerView);
        mAdapter.openLoadAnimation();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });


        refreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getResources()
                        .getDisplayMetrics()));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        viewModel.updateCache();
                    }
                }, 2000);
            }
        });
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        refreshLayout.setRefreshing(true);
    }

    private void subscribeUi() {

        viewModel.getEssayData().observe(this, new Observer<Resource<ZhihuItemEntity>>() {
            @Override
            public void onChanged(@Nullable Resource<ZhihuItemEntity> essayDayEntityResource) {
                if (essayDayEntityResource != null && essayDayEntityResource.data != null) {
                    if (essayDayEntityResource.status == Resource.Status.SUCCEED) {
                        updateUI(essayDayEntityResource.data);
                        Toast.makeText(getActivity(), "succeed", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                }
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateUI(@NonNull ZhihuItemEntity entity) {
        List<EssayListAdapter.MultiItem> list = new ArrayList<>();
        for (ZhihuStoriesEntity enti : entity.getStories()) {
            EssayListAdapter.MultiItem item = new EssayListAdapter.MultiItem(enti, TYPE_BASE);
            list.add(item);
        }
        for (ZhihuStoriesEntity enti : entity.getTop_stories()) {
            EssayListAdapter.MultiItem item = new EssayListAdapter.MultiItem(enti, TYPE_BASE);
            list.add(item);
        }
        mAdapter.replaceData(list);
        getLifecycle().addObserver(new LifecycleObserver() {

            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
    }

}
