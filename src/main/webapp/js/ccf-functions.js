/**
 * Javascript utilities functions that are reused
 */

/*=========================================================================================*/

/*
 * Java script to submit the form when enter key is pressed.
 * 
 */
function submitenter(myfield,e)
		{
		var keycode;
		if (window.event) keycode = window.event.keyCode;
		else if (e) keycode = e.which;
		else return true;

		if (keycode == 13)
		   {
		   myfield.form.submit();
		   return false;
		   }
		else
		   return true;
		}

/*=========================================================================================*/

/*
 * Jquery script to alert the user on unsaved form data on switching tabs,user closes browser window, or 
 * navigation link or submits the form.
 * add class="submit" to the form submit button to have the jquery work properly
 * 
 */

$(document).ready(function() {
    // Set the unload message whenever any input element get changed in the form.
    $(':input').change(function() {
        setConfirmUnload(true);
    });

    // Turn off the unload message whenever a form get submitted properly.
    $('.submit').click(function() {
        setConfirmUnload(false);
    });
});

function setConfirmUnload(on) {
    var message = "You have unsaved changes in the form.";
    window.onbeforeunload = (on) ? function() { return message; } : null;
}


/*=========================================================================================*/