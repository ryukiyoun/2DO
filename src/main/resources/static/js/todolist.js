(function ($){
    let defaults = {
        items: [],
        onItemClick: function (element, event) {}
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

            $(instance.element).addClass('list-group list-group-flush list-group-item-action');

            $.each(instance.options.items, function(index, item){
                let liItem = $('<li/>', {
                    text: item.content,
                    class: 'list-group-item'
                });

                if(index === 0)
                    liItem.addClass('active');

                $(instance.element).append(liItem);
            });

            $(instance.element).on('click', '.list-group-item', function(event){
                if(!$(this).hasClass('active')) {
                    let todoItemActive = $('.list-group-item.active');
                    todoItemActive.removeClass('active');
                    $(this).addClass('active');
                }

                //OPTION CALLBACK
                if( typeof instance.options.onItemClick == 'function' ) {
                    instance.options.onItemClick(instance.element, this);
                }
            });
        },

        getItems: function(){
            return this.options.items;
        }
    };

    $.fn.todolist = function( options ){
        let ret;

        if( !this.length ) {
            return;
        }

        ret = $.data( this, 'plugin_todoList', new TodoList( this, options ) );

        return ret;
    };
}(jQuery));