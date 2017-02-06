package com.f1x.mtcdtools;

/**
 * Created by f1x on 2017-01-28.
 */

public class ListIndexer {
    public ListIndexer() {
        mCurrentIndex = 0;
        mMax = 0;
    }

    public int up() throws IndexOutOfBoundsException {
        if(mMax < 1) {
            throw new IndexOutOfBoundsException();
        }

        if((mCurrentIndex - 1) < 0) {
            mCurrentIndex = mMax -1;
        } else {
            mCurrentIndex--;
        }

        return mCurrentIndex;
    }

    public int down() throws IndexOutOfBoundsException {
        if(mMax < 1) {
            throw new  IndexOutOfBoundsException();
        }

        if((mCurrentIndex + 1) < mMax) {
            mCurrentIndex++;
        } else {
            mCurrentIndex = 0;
        }

        return mCurrentIndex;
    }

    public void reset(int max) {
        mMax = max;
        mCurrentIndex = 0;
    }

    private int mCurrentIndex;
    private int mMax;
}
