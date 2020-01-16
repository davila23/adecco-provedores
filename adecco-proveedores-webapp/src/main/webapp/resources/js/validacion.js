var _needToConfirm = {};
var _needToConfirmOnceIgnore = {};

// Activa la opción de Confirmar deshacer los cambios realizados
function actionConfirmar(formulario) {
	if (typeof(formulario) !== "string")
		throw new Error("No se especifica el id del elemento que contiene los inputs a validar");
	_needToConfirm[formulario] = true;
}

function actionAceptar(formulario) {
	if (formulario === undefined)
		throw new Error("No se especifica el id del elemento que contiene los inputs a validar");
	else if (formulario.nodeType) {
		formulario = jQuery(formulario).is("form") ? jQuery(formulario).attr("id") : jQuery(formulario).closest("form").attr("id");
	}
	_needToConfirm[formulario] = false;
}

// Cancela la acción a ejecutar
function actionCancelar(formulario) {
	if (formulario === undefined)
		throw new Error("No se especifica el id del elemento que contiene los inputs a validar");
	else if (formulario.nodeType) {
		formulario = jQuery(formulario).is("form") ? jQuery(formulario).attr("id") : jQuery(formulario).closest("form").attr("id");
	}
	if (_needToConfirmOnceIgnore[formulario] == true) {
		_needToConfirmOnceIgnore[formulario] = false;
	}
	else if (_needToConfirm[formulario]) {
		if (!confirm('Tiene cambios en la página, si la abandona puede perder los cambios. ¿Está seguro de querer cancelar?')) {
			return true;
		}
		_needToConfirm[formulario] = false;
	}
	return false;
}

//Ignora la confirmación temporalmente
function actionIgnorarUnaVez(formulario) {
	if (typeof(formulario) !== "string")
		throw new Error("No se especifica el id del elemento que contiene los inputs a validar");
	_needToConfirmOnceIgnore[formulario] = true;
}

function init_validacion(formulario, soloUnload) {
	if (typeof(formulario) !== "string")
		throw new Error("No se especifica el id del elemento que contiene los inputs a validar");
	
	if (soloUnload == undefined)
		soloUnload = false;
	
	if (!soloUnload) {
		var $form = jQuery("#" + formulario);
//		if ($form.length == 0) // Compatibilidad
//			$form = jQuery("body");
		
//		$form
//			//.find(":input[type=text],:select")
//			.find("input,select,textarea")
//			.not(".input-ignorar-cambios")
//			.not(".input-ignorar-cambios input,.input-ignorar-cambios select,.input-ignorar-cambios textarea")
//			.not("[type='hidden'],[type='button'],[type='submit'],[type='reset']")
//			.change(function() {actionConfirmar(formulario);});
		
		// .on: Permite incorporar a nuevos elementos del DOM (cargados dinamicamente).
		$form.on('change', "input:not(.input-ignorar-cambios, .input-ignorar-cambios input, [type='hidden'], [type='button'], [type='submit'], [type='reset'], .ui-paginator input), " + 
							"select:not(.input-ignorar-cambios, .input-ignorar-cambios select, .ui-paginator select), " +
							"textarea:not(.input-ignorar-cambios, .input-ignorar-cambios textarea, .ui-paginator textarea)",
							function() { actionConfirmar(formulario); }
		);
	}
	
	window.onbeforeunload = function () {
		if (_needToConfirmOnceIgnore[formulario] == true) {
			_needToConfirmOnceIgnore[formulario] = false;
		}
		else if (_needToConfirm[formulario])  {
			return "Tiene datos sin grabar. Puede llegar a perder algunos datos. ¿Está seguro que se quiere ir de esta página?";
		}
	};
}
