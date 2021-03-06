(function(){
	function bindDelete() {
		var aTag = document.querySelectorAll("a[data-method='delete']");
		aTag.forEach(a => {
			a.addEventListener("click", e => {
				if (!confirm("Are you sure?")) {
					return;
				}

				var form = document.createElement("form");
				form.setAttribute("action", e.target.getAttribute("href"));
				form.setAttribute("method", "post");
				
				var i1 = document.createElement("input");
				i1.setAttribute("type", "hidden");
				i1.setAttribute("name", "_csrf");
				i1.setAttribute("value", csrfToken());
				form.appendChild(i1);
				
				var i2 = document.createElement("input");
				i2.setAttribute("type", "hidden");
				i2.setAttribute("name", "_method");
				i2.setAttribute("value", e.target.dataset['method']);
				form.appendChild(i2);

				e.preventDefault();
				document.body.appendChild(form);
				form.submit();
			});
		});
	}
	
	function csrfToken() {
		return document.querySelector("meta[name='_csrf']").getAttribute('content');
	}
	
	window.addEventListener("DOMContentLoaded", bindDelete);
})();
