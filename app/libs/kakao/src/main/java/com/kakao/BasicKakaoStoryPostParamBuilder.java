/**
 * Copyright 2014 Kakao Corp.
 *
 * Redistribution and modification in source or binary forms are not permitted without specific prior written permission. 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kakao;

import android.os.Bundle;
import com.kakao.helper.ServerProtocol;

/*
 * Param Name	        Param Type	필수      설명
 * permission	        enum	    x       A : 전체 공개   F : 친구 공개. default A.
 * shareable	        Boolean	    x	    친구 공개시 포스팅 공유 허용 여부  default false.
 * android_exec_param	String	    X       안드로이드 custom url에 붙일 파라미터 kakao[appkey]://kakaostory + "?" + ${android_exec_param}
 * ios_exec_param	    String	    X	    아이폰 custom url에 붙일 파라미터 kakao[appkey]://kakaostory + "?" + ${ios_exec_param}
*/

/**
 * 스토리 포스트시 필요한 정보를 구성하는 기본 Builder이다.
 * 공개범위, 공유여부, 앱연결시 추가 param. <br/>
 * 공개범위, 공유여부는 필수 항목이다. <br/>
 * @author MJ
*/
public abstract class BasicKakaoStoryPostParamBuilder {
    /**
     * 스토리 포스트시 공개 범위
     */
    public enum PERMISSION {
        /**
         * 전체 공개
         */
        PUBLIC("A"),
        /**
         * 친구 공개
         */
        FRIENDS("F");

        private final String value;

        PERMISSION(String value) {
            this.value = value;
        }
    }

    private PERMISSION permission;
    private Boolean shareable;
    private String androidExecuteParam;
    private String iosExecuteParam;

    /**
     * 기본으로 전체공개로 파마미터 빌더를 생성한다.
     */
    protected BasicKakaoStoryPostParamBuilder() {
    }

    /**
     * 기본은 전체공개인데 포스팅할 스토리의 공개 여부를 변경하고 싶을 때 설정한다.
     * @param permission 공개 종류. {@link com.kakao.BasicKakaoStoryPostParamBuilder.PERMISSION#FRIENDS} 또는 {@link com.kakao.BasicKakaoStoryPostParamBuilder.PERMISSION#PUBLIC}
     */
    public BasicKakaoStoryPostParamBuilder setPermission(final PERMISSION permission) {
        this.permission = permission;
        return this;
    }

    /**
     * 친구 공개 포스팅에 대해 공유 여부를 설정한다.
     * @param shareable 공유 가능하게 하려면 true, 공유 불가능하게 하려면 false
     */
    public BasicKakaoStoryPostParamBuilder setShareable(final boolean shareable) {
        this.shareable = shareable;
        return this;
    }

    /**
     * Android 앱연결 링크에 추가할 파라미터 설정한다.
     * 기본적으로는 kakao[appkey]://kakaostory로 연결되나 그 뒤에 파라미터를 붙이고 싶을 때 사용한다.
     * @param androidExecuteParam 추가할 파라미터
     * @return 파라미터 추가후 builder
     */
    public BasicKakaoStoryPostParamBuilder setAndroidExecuteParam(final String androidExecuteParam) {
        this.androidExecuteParam = androidExecuteParam;
        return this;
    }
    /**
     * iOS 앱연결 링크에 추가할 파라미터 설정한다.
     * 기본적으로는 kakao[appkey]://kakaostory로 연결되나 그 뒤에 파라미터를 붙이고 싶을 때 사용한다.
     * @param iosExecuteParam 추가할 파라미터
     * @return 파라미터 추가후 builder
     */
    public BasicKakaoStoryPostParamBuilder setIOSExecuteParam(final String iosExecuteParam) {
        this.iosExecuteParam = iosExecuteParam;
        return this;
    }

    /**
     * 지금까지 추가된 설정을 Bundle로 만들어준다.
     * @return 스토리 포스트 설정을 Bundle로 반환
     */
    public Bundle build() throws KakaoParameterException {
        final Bundle parameters = new Bundle();
        if(permission != null)
            parameters.putString(ServerProtocol.PERMISSION_KEY, permission.value);
        if(shareable != null)
            parameters.putBoolean(ServerProtocol.ENABLE_SHARE_KEY, shareable);
        if(androidExecuteParam != null)
            parameters.putString(ServerProtocol.ANDROID_EXEC_PARAM_KEY, androidExecuteParam);
        if(iosExecuteParam != null)
            parameters.putString(ServerProtocol.IOS_EXEC_PARAM_KEY, iosExecuteParam);
        return parameters;
    }
}
