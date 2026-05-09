const fetchHelper = {
    __request: async function (url, method = "get", params = null, loader = '#loader') {
        console.group("FetchHelper ::: %s", new Date().toLocaleString());

        // 요청 URL이 URL객체가 아닌 경우 객체 생성
        if (url.constructor !== URL) {
            // 프론트엔드 모드로 작동할 때
            if (typeof window !== "undefined") {
                if (url.substring(0, 4) !== "http") {
                    if (url.substring(0, 1) === "/") {
                        url = window.location.origin + url;
                    } else {
                        url = window.location.origin + "/" + url;
                    }
                    url = new URL(url);
                } else {
                    url = new URL(url);
                }
            }
            // 백엔드 모드로 작동할 때
            else {
                url = new URL(url);
            }
        }

        // post, put, delete 방식에서 파라미터가 FormData 객체가 아닌 경우 객체 변환
        if (method.toLocaleLowerCase() !== "get" && params) {
            if (typeof window !== "undefined")  {
                if (params.constructor !== FormData) {
                    switch (params.constructor) {
                        case SubmitEvent:
                            params = new FormData(params.currentTarget);
                            break;
                        case HTMLFormElement:
                            params = new FormData(params);
                            break;
                        default:
                            const tmp = structuredClone(params);
                            params = new FormData();

                            for (const t in tmp) {
                                const value = tmp[t];
                                if (value) {
                                    params.set(t, value);
                                }
                            }
                            break;
                    }
                }
            } else {
                if (params.constructor !== Object) {
                    params = Object.fromEntries(params);
                }

                const tmp = structuredClone(params);
                params = new FormData();

                for (const t in tmp) {
                    const value = tmp[t];
                    if (value) {
                        params.set(t, value);
                    }
                }
            }
        }

        let result = null;      // Ajax 연동 결과가 저장될 객체
        let options = null;     // post, put, delete에서 사용할 옵션 변수

        if (method.toLocaleLowerCase() !== "get") {
            options = {
                method: method,
                body: params,
            };
        }

        if (loader && typeof document !== "undefined") {
            if (typeof loader == 'string') {
                loader = document.querySelector(loader);
            }

            if (loader) {
                loader.style.display = 'block';
            }
        }

        try {
            // 백엔드로부터의 응답 받기
            console.log(`→ [${method}] ${url}`);
            const response = await fetch(url, options);

            // 백엔드가 에러를 보내왔다면?
            if (parseInt(response.status / 100) !== 2) {
                // 에러 객체 생성후 에러 발생 --> catch로 이동함
                const err = new Error(response.statusText);
                err.json = await response.json();
                throw err;
            }

            // 응답으로부터 JSON 데이터 추출
            result = await response.json();
        } catch (err) {
            console.error(JSON.stringify(err.json));
            console.groupEnd();

            if (err?.json?.message) {
                err.message = err.json.message;
            }

            throw err;
        } finally {
            // 프론트엔드 모드로 작동할 때 로더 숨기기
            if (loader && typeof document !== "undefined") {
                loader.style.display = 'none';
            }
        }

        console.log("← %o", result);
        console.groupEnd();

        return result;
    },
    get: async function(urlString, params, loader) {
        let url = null;

        if (urlString.substring(0, 4) !== "http") {
            if (urlString.substring(0, 1) === "/") {
                urlString = window.location.origin + urlString;
            } else {
                urlString = window.location.origin + "/" + urlString;
            }
            url = new URL(urlString);
        } else {
            url = new URL(urlString);
        }

        // params를 url에 QueryString형태로 추가해야 함
        if (params) {
            // 클라이언트 환경에서의 처리
            if (typeof window !== "undefined")  {
                switch (params.constructor) {
                    case SubmitEvent:
                        params = new FormData(params.currentTarget);
                        break;
                    case HTMLFormElement:
                        params = new FormData(params);
                        break;
                    default:
                        break;
                }

                if (params.constructor === FormData) {
                    for (const p of params.keys()) {
                        const value = params.get(p);
                        if (value) {
                            url.searchParams.set(p, value);
                        }
                    }
                } else {
                    for (const p in params) {
                        const value = params[p];
                        if (value) {
                            url.searchParams.set(p, params[p]);
                        }
                    }
                }
            } else {
                // 서버 환경에서의 처리
                if (params.constructor === Object) {
                    console.log(`params: %o`, params);
                    for (const p in params) {
                        const value = params[p];
                        if (value) {
                            url.searchParams.set(p, params[p]);
                        }
                    }
                } else if (params.constructor === FormData) {
                    for (const p of params.keys()) {
                        const value = params.get(p);
                        if (value) {
                            url.searchParams.set(p, value);
                        }
                    }
                }
            }
        }

        return await this.__request(url, "get", null, loader);
    },
    post: async function(urlString, params, loader) {
        // params를 FormData객체 형태로 변환해야 함
        return await this.__request(urlString, "post", params, loader);
    },
    put: async function(urlString, params, loader) {
        // params를 FormData객체 형태로 변환해야 함
        return await this.__request(urlString, "put", params, loader);
    },
    delete: async function(urlString, params, loader) {
        // params를 FormData객체 형태로 변환해야 함
        return await this.__request(urlString, "delete", params, loader);
    }
}