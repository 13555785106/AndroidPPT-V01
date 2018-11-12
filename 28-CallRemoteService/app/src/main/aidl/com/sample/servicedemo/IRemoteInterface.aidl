// IRemoteInterface.aidl
package com.sample.servicedemo;
import com.sample.servicedemo.AidlRequest;
interface IRemoteInterface {
    boolean sendRequest(in AidlRequest aidlRequest);
}