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
                class: 'list-group list-group-flush list-group-item-action'
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
                            text: item.contents,
                            class: 'list-group-item',
                            style: 'cursor: pointer'
                        });

                        liItem.data('todoId', item.id);

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