(function(){
	function bindLink() {
		var aTag = document.querySelectorAll("a[data-method='post'], a[data-method='delete'], a[data-method='put'], a[data-method='patch']");
		aTag.forEach(a => {
			a.addEventListener("click", e => {
				if (!confirm("Are you sure?")) {
					e.preventDefault();
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
				
				var method = e.target.dataset['method'];
				if (!(method.toLowerCase() === 'post')) {
					var i2 = document.createElement("input");
					i2.setAttribute("type", "hidden");
				 	i2.setAttribute("name", "_method");
					i2.setAttribute("value", method);
					form.appendChild(i2);
				}

				e.preventDefault();
				document.body.appendChild(form);
				form.submit();
			});
		});
	}
	
	function csrfToken() {
		return document.querySelector("meta[name='_csrf']").getAttribute('content');
	}
	
	window.addEventListener("DOMContentLoaded", bindLink);
})();
