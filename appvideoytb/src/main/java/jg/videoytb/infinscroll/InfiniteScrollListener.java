package jg.videoytb.infinscroll;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

abstract public class InfiniteScrollListener implements OnScrollListener {

	protected static final int DEFAULT_MIN_ITEMS_LEFT = 10;
	protected static final int DEFAULT_MAX_PAGES = 4;
	protected final int mMinItemsLeft;
	protected final int mMaxPages;
	protected int mPagesLoaded;
	protected int mItemsTotal;
	protected boolean mIsLoading;
	protected boolean mEnabled;

	public InfiniteScrollListener() {
		this(DEFAULT_MIN_ITEMS_LEFT);
	}

	public InfiniteScrollListener(int minItemsLeft) {
		this(minItemsLeft, DEFAULT_MAX_PAGES);
	}

	public InfiniteScrollListener(int minItemsLeft, int maxPages) {
		mMinItemsLeft = minItemsLeft;
		mMaxPages = maxPages;
		mPagesLoaded = -1;
		mItemsTotal = 0;
		mIsLoading = true;
		mEnabled = true;
	}

	abstract public void onReloadItems(int pageToRequest);
	abstract public void onReloadFinished();

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int itemsTotal) {
		if (mEnabled) {
			if (mIsLoading) {
				if (itemsTotal > mItemsTotal) {
					onReloadFinished();
					mIsLoading = false;
					mItemsTotal = itemsTotal;
					mPagesLoaded++;
				}
			}
			else if (mPagesLoaded < mMaxPages) {
				if ((firstVisibleItem+visibleItemCount) > (itemsTotal-mMinItemsLeft)) {
					onReloadItems(mPagesLoaded+1);
					mIsLoading = true;
				}
			}
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) { }

	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}

}