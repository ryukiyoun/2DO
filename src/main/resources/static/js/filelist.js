(function ($){
    let defaults = {
        files: [],
        server: {}
    }

    function FileList( element, options )
    {
        this.element           = element;
        this.options           = $.extend( true, {}, defaults, options );

        this.load();
    }

    FileList.prototype = {
        load: function() {
            let instance = this;

            this.makeElement(instance);

            $('#emptyFileImg').attr('width', $('#fileList').width() * 0.8);

            $(instance.element).on('click', '.card.my-1', function(event){
                location.href = $(this).data('file');
            });

            $(window).resize(function() {
                $('#emptyFileImg').attr('width', $('#fileList').width() * 0.8);
            });
        },

        makeElement(instance){
            let fileWrapper = $('<div/>', {
                class: 'd-flex flex-column'
            });

            let titleWrapper = $('<div/>', {
               class: 'd-flex mb-2 align-items-center justify-content-between'
            });

            let title = $('<p/>', {
                text: '파일 다운',
                class: 'mb-0',
                style: 'font-size: 13px;'
            });

            let allBtn = $('<button/>', {
                text: 'all',
                class: 'btn btn-secondary btn-sm',
                style: 'font-size: 13px;'
            });

            titleWrapper.append(title);
            titleWrapper.append(allBtn);

            fileWrapper.append(titleWrapper);

            let fileListWrapper = $('<div/>', {
                id: 'fileListW',
                class: 'overflow-auto d-none',
                style: 'min-height: 16em;'
            })

            $(instance.element).append(fileWrapper);
            $(instance.element).append(fileListWrapper);

            let emptyWrapper = $('<div/>', {
                id:'fileEmptyW',
                class: 'overflow-hidden d-flex flex-column flex-1 justify-content-center align-items-center'
            })

            let emptyImg = $('<img/>', {
                id: 'emptyFileImg',
                src: '/static/img/empty-file.png'
            })

            emptyWrapper.append(emptyImg);

            $(instance.element).append(emptyWrapper);
        },

        findFileList(todoId){
            let instance = this;

            ajaxGetRequest('/files/' + todoId, {}, function(data){
                $('#fileListW').empty();

                if(data.length !== 0){
                    $('#fileEmptyW').removeClass('d-flex');
                    $('#fileEmptyW').addClass('d-none');
                    $('#fileListW').removeClass('d-none');

                    $.each(data, function(index, item){
                        let card = $('<div/>', {
                            class: 'card my-1',
                            style: 'cursor:pointer;'
                        });

                        card.data('file', '/file/download/' + item.id);

                        let cardBody = $('<div/>', {
                            class: 'card-body p-2 d-flex align-items-center'
                        });

                        let icon = $('<i/>', {
                            class: FileList.prototype.checkExtension(item.extension),
                            style: 'font-size: 2.5em;'
                        });

                        let margin = $('<div/>', {
                            class: 'm-2'
                        });

                        let text = $('<p/>', {
                            text: item.originName,
                            class:'m-0 cut-text',
                            style: 'font-size: 13px;'
                        });

                        cardBody.append(icon);
                        cardBody.append(margin);
                        cardBody.append(text);

                        card.append(cardBody);

                        $('#fileListW').append(card);
                    });
                }
                else
                    instance.setEmpty();
            }, null);
        },

        setEmpty(){
            $('#fileEmptyW').removeClass('d-none');
            $('#fileEmptyW').addClass('d-flex');
            $('#fileListW').addClass('d-none');
        },

        checkExtension(extension){
            switch(extension){
                case '.png':
                    return 'bi bi-filetype-png'
                case '.jpg':
                    return 'bi bi-filetype-jpg'
                case '.gif':
                    return 'bi bi-filetype-gif'
                case '.txt':
                    return 'bi bi-filetype-txt'
                case '.pdf':
                    return 'bi bi-filetype-pdf'
                case '.ppt':
                    return 'bi bi-filetype-ppt'
                case '.pptx':
                    return 'bi bi-filetype-pptx'
                case '.doc':
                    return 'bi bi-filetype-doc'
                case '.docx':
                    return 'bi bi-filetype-docx'
                case '.xls':
                    return 'bi bi-filetype-xls'
                case '.xlsx':
                    return 'bi bi-filetype-xlsx'
                default:
                    return 'bi bi-file-earmark'
            }
        }
    }
    ;

    $.fn.fileList = function( options ){
        let ret;

        if( !this.length ) {
            return;
        }

        ret = $.data( this, 'plugin_fileList', new FileList( this, options ) );

        return ret;
    };
}(jQuery));