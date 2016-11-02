// aidlApi.aidl
package com.example.skyclad.mysqltest;

import com.example.skyclad.mysqltest.ItemListener;
import com.example.skyclad.mysqltest.User;
//import com.example.skyclad.mysqltest.Item;
// Declare any non-default types here with import statements

interface AidlApi {
    User getUser();
    void addListener(ItemListener listener);
    void removeListener(ItemListener listener);
}
