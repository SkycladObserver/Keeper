// aidlApi.aidl
package com.example.skyclad.mysqltest;

import com.example.skyclad.mysqltest.ItemListener;
import com.example.skyclad.mysqltest.ParcelableItem;
// Declare any non-default types here with import statements

interface AidlApi {
    User getParcelableItem();
    void addListener(ItemListener listener);
    void removeListener(ItemListener listener);
}
