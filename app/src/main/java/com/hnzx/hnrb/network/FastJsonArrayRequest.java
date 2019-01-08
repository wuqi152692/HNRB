package com.hnzx.hnrb.network;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.hnzx.hnrb.responsebean.BaseBeanArrayRsp;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class FastJsonArrayRequest<T> extends Request<BaseBeanArrayRsp<T>> {
    private final TypeReference<BaseBeanArrayRsp<T>> mClazz;
    private final Listener<BaseBeanArrayRsp<T>> mListener;
    private final Map<String, String> mHeaders;
    private Map<String, String> mParams;

    public FastJsonArrayRequest(String url, TypeReference<BaseBeanArrayRsp<T>> clazz,
                                Listener<BaseBeanArrayRsp<T>> listener, ErrorListener errorListener) {
        this(Method.GET, url, clazz, null, listener, errorListener);
    }

    public FastJsonArrayRequest(Map<String, String> mParams, String url, TypeReference<BaseBeanArrayRsp<T>> clazz,
                                Listener<BaseBeanArrayRsp<T>> listener, ErrorListener errorListener) {
        this(Method.POST, url, clazz, null, listener, errorListener);
        this.mParams = mParams;
    }

    public FastJsonArrayRequest(int method, String url, TypeReference<BaseBeanArrayRsp<T>> clazz,
                                Map<String, String> headers, Listener<BaseBeanArrayRsp<T>> listener,
                                ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mClazz = clazz;
        this.mHeaders = headers;
        this.mListener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return mHeaders != null ? mHeaders : super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams != null ? mParams : super.getParams();
    }

    @Override
    protected void deliverResponse(BaseBeanArrayRsp<T> response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<BaseBeanArrayRsp<T>> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(JSON.parseObject(json, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }
}