const csrfToken = $("meta[name='_csrf']").attr("content");

function ajaxGetRequest(requestURL, requestParameter, successCallBack, failCallBack){
    $.ajax({
        type: 'GET',
        url: requestURL,
        data: requestParameter,
        contentType: 'application/json',
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-Ajax-Request', true);
        },
        success: function (data) {
            if(successCallBack != null)
                successCallBack(data);
        },
        error: function (data) {
            commonErrorProcess(data);
            if(failCallBack != null)
                failCallBack(data);
        }
    });
}

function ajaxPostRequest(requestURL, requestBody, successCallBack, failCallBack){
    $.ajax({
        type: 'POST',
        url: requestURL,
        data: requestBody,
        contentType: 'application/json',
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-Ajax-Request', true);
            xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken);
        },
        success: function (data) {
            if(successCallBack != null)
                successCallBack(data);
        },
        error: function (data) {
            commonErrorProcess(data);
            if(failCallBack != null)
                failCallBack(data);
        }
    });
}

function ajaxDeleteRequest(requestURL, requestParameter, successCallBack, failCallBack){
    $.ajax({
        type: 'DELETE',
        url: requestURL,
        data: requestParameter,
        contentType: 'application/json',
        dataType: 'json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader('X-Ajax-Request', true);
            xhr.setRequestHeader('X-XSRF-TOKEN', csrfToken);
        },
        success: function (data) {
            if(successCallBack != null)
                successCallBack(data);
        },
        error: function (data) {
            commonErrorProcess(data);
            if(failCallBack != null)
                failCallBack(data);
        }
    });
}

function commonErrorProcess(data){
    if(data.status === 403){
        sweetAlertButton.fire({
            allowOutsideClick: false,
            allowEscapeKey: false,
            icon: 'error',
            title: '세션 만료',
            text: '로그인 페이지로 이동 합니다.'
        }).then((result) => {
            if (result.isConfirmed)
                location.href = '/login';
        })
    }
}