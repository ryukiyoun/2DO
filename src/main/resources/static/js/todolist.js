(function ($){
    let defaults = {
        items: [],
        server: {},
        onItemClick: function (element, event) {},
        onFindTodoList: function(element, data) {},
    }

    function TodoList( element, options )
    {
        this.element           = element;
        this.options           = $.extend( true, {}, defaults, options );

        this.load();
    }

    TodoList.prototype = {
        load: function() {
            let instance = this;

            this.makeElement(instance);

            this.setTodoDate(new Date());

            this.findTodoList(new Date());

            $(instance.element).on('click', '.list-group-item', function(event){
                if(!$(this).hasClass('active')) {
                    let todoItemActive = $('.list-group-item.active');
                    todoItemActive.removeClass('active');
                    $(this).addClass('active');

                    //OPTION CALLBACK
                    if( typeof instance.options.onItemClick == 'function' ) {
                        instance.options.onItemClick(instance.element, this);
                    }
                }
            });

            $(instance.element).on('keydown', '#todoInput', function(keyCode) {
                if(keyCode.keyCode === 13 && $(this).val() !== ''){
                    ajaxPostRequest('/2do', JSON.stringify({
                            todoDate: $('#todoDateText').data('todoDate'),
                            contents:$(this).val(),
                            state:'NORMAL'
                        }),
                        function() {
                            $('#todoInput').val('');
                            instance.findTodoList(new Date($('#todoDateText').data('todoDate')));
                        }, null);
                }
            });

            $(instance.element).on('keydown', '#todoListEl', function(keyCode) {
                let element;

                if(keyCode.keyCode === 38)
                    element = $(this).find('.active').prev();
                else if(keyCode.keyCode === 40)
                    element = $(this).find('.active').next();

                if(element.length !== 0){
                    element.focus();
                    element.click();
                }
            });
        },

        makeElement(instance){
            let todoDateText = $('<h2/>', {
                id: 'todoDateText',
                class: 'mb-3',
                text: new Date().getFullYear() + '년 ' + (new Date().getMonth() + 1) + '월 ' + new Date().getDate() + '일'
            });

            $(instance.element).append(todoDateText);

            let todoInput = $('<input/>', {
                id: 'todoInput',
                class: 'form-control mb-2',
                type: 'text',
                placeholder: '신규 할 일'
            });

            $(instance.element).append(todoInput);

            let card = $('<div>', {
                class: 'card mb-2 d-flex flex-column flex-1 overflow-auto'
            });

            let cardBody = $('<div>', {
                class: 'card-body p-3 d-flex flex-column'
            })

            let ulWrapper = $('<div/>', {
                id:'todoListW',
                class: 'overflow-auto'
            });

            let ul = $('<ul/>', {
                id: 'todoListEl',
                class: 'list-group list-group-flush list-group-item-action',
                tabindex: '0'
            });

            let emptyWrapper = $('<div/>', {
                id:'todoEmptyW',
                class: 'flex-1 align-items-center justify-content-center d-none'
            });

            let emptyImg = $('<img/>', {
                src:'/static/img/empty-todo.png'
            });

            emptyWrapper.append(emptyImg);

            ulWrapper.append(ul);

            cardBody.append(ulWrapper);
            cardBody.append(emptyWrapper);

            card.append(cardBody);

            $(instance.element).append(card);
        },

        findTodoList(date){
            let instance = this;
            ajaxGetRequest('/2do/' + toStringByFormatting(date, ''), {}, function(data){
                $('#todoListEl').empty();

                if(data.length !== 0) {
                    $('#todoListW').removeClass('d-none');
                    $('#todoEmptyW').removeClass('d-flex');
                    $('#todoEmptyW').addClass('d-none');

                    $.each(data, function (index, item) {
                        let liItem = $('<li/>', {
                            class: 'list-group-item d-flex align-items-center',
                            style: 'cursor: pointer'
                        });

                        let p = $('<p/>', {
                            text: item.contents,
                            class: 'm-0'
                        });

                        let termDiv = $('<div/>', {
                            class: 'm-auto'
                        });

                        let btnDiv = $('<div/>', {
                            class: 'd-flex'
                        });

                        let normalBtn = $('<p/>', {
                            class: 'm-0'
                        });

                        let normalSVG = $('<svg/>', {
                            xmlns: 'http://www.w3.org/2000/svg',
                            width: '16',
                            height: '16',
                            fill: 'currentColor',
                            class: 'bi bi-bookmark',
                            viewBox: '0 0 16 16'
                        });

                        let normalPath = $('<path/>', {
                            d: 'M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v13.5a.5.5 0 0 1-.777.416L8 13.101l-5.223 2.815A.5.5 0 0 1 2 15.5V2zm2-1a1 1 0 0 0-1 1v12.566l4.723-2.482a.5.5 0 0 1 .554 0L13 14.566V2a1 1 0 0 0-1-1H4z'
                        });

                        normalSVG.append(normalPath);

                        normalBtn.append(normalSVG);

                        let cancelBtn = $('<button/>', {
                            class: 'btn btn-outline-secondary mr-2'
                        });

                        let cancelSVG = $('<svg/>', {
                            xmlns: 'http://www.w3.org/2000/svg',
                            width: '16',
                            height: '16',
                            fill: 'currentColor',
                            class: 'bi bi-bookmark',
                            viewBox: '0 0 16 16'
                        });

                        let cancelPath = $('<path/>', {
                            d: 'M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v13.5a.5.5 0 0 1-.777.416L8 13.101l-5.223 2.815A.5.5 0 0 1 2 15.5V2zm2-1a1 1 0 0 0-1 1v12.566l4.723-2.482a.5.5 0 0 1 .554 0L13 14.566V2a1 1 0 0 0-1-1H4z'
                        });

                        cancelSVG.append(cancelPath);

                        cancelBtn.append(cancelSVG);

                        let completeBtn = $('<button/>', {
                            class: 'btn btn-outline-secondary'
                        });

                        let completeSVG = $('<svg/>', {
                            xmlns: 'http://www.w3.org/2000/svg',
                            width: '16',
                            height: '16',
                            fill: 'currentColor',
                            class: 'bi bi-bookmark',
                            viewBox: '0 0 16 16'
                        });

                        let completePath = $('<path/>', {
                            d: 'M2 2a2 2 0 0 1 2-2h8a2 2 0 0 1 2 2v13.5a.5.5 0 0 1-.777.416L8 13.101l-5.223 2.815A.5.5 0 0 1 2 15.5V2zm2-1a1 1 0 0 0-1 1v12.566l4.723-2.482a.5.5 0 0 1 .554 0L13 14.566V2a1 1 0 0 0-1-1H4z'
                        });

                        completeSVG.append(completePath);

                        completeBtn.append(completeSVG);

                        liItem.append(p);

                        liItem.append(termDiv);

                        liItem.append(btnDiv);

                        btnDiv.append(normalBtn);

                        liItem.data('todoId', item.id);
                        liItem.data('state', item.state);
                        liItem.data('progress', item.progress);

                        $('#todoListEl').append(liItem);

                        if (index === 0)
                            liItem.click();
                    });
                }
                else{
                    $('#todoListW').addClass('d-none');
                    $('#todoEmptyW').removeClass('d-none');
                    $('#todoEmptyW').addClass('d-flex');
                }

                //OPTION CALLBACK
                if( typeof instance.options.onFindTodoList == 'function' ) {
                    instance.options.onFindTodoList(instance.element, data);
                }

            }, null);
        },

        getActiveTodoId(){
            return $('.list-group-item.active').data('todoId');
        },

        getActiveTodo(){
            return {
                id: $('.list-group-item.active').data('todoId'),
                state: $('.list-group-item.active').data('state'),
                progress: $('.list-group-item.active').data('progress'),
            };
        },

        updateActiveProgress(progress){
            $('.list-group-item.active').data('progress', progress);
        },

        updateActiveState(state){
            $('.list-group-item.active').data('state', state);
        },

        setTodoDate(date){
            let year = date.getFullYear();
            let month = date.getMonth() + 1;
            let day = date.getDate();

            $('#todoDateText').text(year + '년 ' + month + '월 ' + day + '일');
            $('#todoDateText').data('todoDate', year + '-' + ('0' + month).slice(-2) + '-' + ('0' + day).slice(-2));
        }
    };

    $.fn.todoList = function( options ){
        let ret;

        if( !this.length ) {
            return;
        }

        ret = $.data( this, 'plugin_todoList', new TodoList( this, options ) );

        return ret;
    };
}(jQuery));