/**
 * Created by Kir Kolesnikov on 12.02.2015.
 */

$(document).ready(function () { // вся магия после загрузки страницы
    var dbutton = $('#deposit-button');
    var wbutton = $('#withdraw-button');

    dbutton.click(function (event) {
        event.preventDefault(); // выключаем стандартную роль элемента
        document.getElementById("variable").innerHTML = dbutton.val() + " funds";
        $('#overlay').fadeIn(400, // сначала плавно показываем темную подложку
            function () { // после выполнения предъидущей анимации
                $('#modal_form')
                    .css('display', 'block') // убираем у модального окна display: none;
                    .animate({opacity: 1, top: '50%'}, 200); // плавно прибавляем прозрачность одновременно со съезжанием вниз
            });
    });

    wbutton.click(function (event) {
        event.preventDefault(); // выключаем стандартную роль элемента
        document.getElementById("variable").innerHTML = wbutton.val() + " funds";
        $('#overlay').fadeIn(400, // сначала плавно показываем темную подложку
            function () { // после выполнения предъидущей анимации
                $('#modal_form')
                    .css('display', 'block') // убираем у модального окна display: none;
                    .animate({opacity: 1, top: '50%'}, 200); // плавно прибавляем прозрачность одновременно со съезжанием вниз
            });
    });


    /* Закрытие модального окна, тут делаем то же самое но в обратном порядке */
    $('#modal_close, #overlay').click(function () { // ловим клик по крестику или подложке
        $('#modal_form')
            .animate({opacity: 0, top: '45%'}, 200,  // плавно меняем прозрачность на 0 и одновременно двигаем окно вверх
            function () { // после анимации
                $(this).css('display', 'none'); // делаем ему display: none;
                $('#overlay').fadeOut(400); // скрываем подложку
            }
        );
    });
});


