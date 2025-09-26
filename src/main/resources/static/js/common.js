function addItemToCart( productid ){
	 var isConfirme = confirm("Do you want this product to add to cart?");
	 
	 if ( isConfirme ) {
		let count = parseInt($("#count_badge").text(), 10) + 1;
		
		$("#count_badge").text(count);
		if ($("#count_badge").css("display") === "none") {
			$("#count_badge").show(); 
		}
		if ($("#count_circle").css("display") === "none") {
			$("#count_circle").show(); 
		}
		
		setTimeout(() => {
		  console.log("1 second has passed!");
		}, 1000);

		$.ajax({
	        url: "http://localhost:8080/setCartItems/" + productid, // The URL of your server-side endpoint
	        type: "POST",
	        dataType: "text", // Expecting JSON response
	        success: function(response) {
	            if (response != null && response != "") {
	                console.log("Item from session: " + response);
	            } else {
	                console.log("Item not found in session.");
	            }
	        },
	        error: function(jqXHR, textStatus, errorThrown) {
	            console.error("Error fetching session data: " + textStatus, errorThrown);
	        }
		});
		
    }
	 
}

function createSession()
{
	var name = document.getElementById("name").value;
	var password = document.getElementById("pwd").value;
	
	if(password == 1234 ){	
		return true;
	}
	else{
		alert("Credentials are incorrect!");
	}
	
	return false;
}

