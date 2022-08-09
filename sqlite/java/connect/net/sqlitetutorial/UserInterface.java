import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.ResultSet;
import java.util.*;


public class UserInterface {
	static Scanner ur = new Scanner(System.in);

	public static void chooseInterface(Statement stmt) throws  SQLException{
		System.out.print("Press 1 to log into eMart, press 2 to log into eDepot, press 0 to exit\n");
        String str = ur.nextLine();
        if (str.equals("1")){
        	EmartLogin(stmt);
        }
		else if (str.equals("2")){
        	EdepotLogin(stmt);
        }
		else if (str.equals("0")){
			System.out.print("Goodbye!\n");
        	//System.exit(0);
        }
		else{
        	System.out.println("Invalid input!");
        	chooseInterface(stmt);
        }
	}

    public static void EmartLogin( Statement stmt) throws SQLException{
		List<String> searchParameters = new ArrayList<>();
		searchParameters.add("stock number: ");
		searchParameters.add("manufacturer: ");
		searchParameters.add("model number: ");
		searchParameters.add("category: ");
		searchParameters.add("description attribute: ");
		searchParameters.add("description value: ");

		System.out.print("press 1 to log in, press 0 to go back\n");
        String str = ur.nextLine();

		if(str.equals("1")){

			System.out.print("Enter username: ");
			String username = ur.nextLine();

			System.out.print("Enter password: ");
			String password = ur.nextLine();

			//System.out.println(loginQuery);
			String loginQuery = "SELECT * FROM EMARTCUSTOMERS WHERE CID = '" + username + "' AND PASSWORD = '" + password + "'";
			
			ResultSet rs = stmt.executeQuery(loginQuery);

			boolean qr = rs.next();
			String userStatus = rs.getString(6);
			int managerStatus = rs.getInt(7);

			//regular customer
			if(qr && managerStatus==0){
				
				System.out.println("Welcome back to eMart " + username + "!");
				
				String userInput;
				int userInputToInt = -1;
				while(userInputToInt != 0){
					List<String> holdParameters = new ArrayList<>();
					System.out.println("Press 1 to search for items");
					System.out.println("Press 2 to view shopping cart");
					System.out.println("Press 3 to checkout");
					System.out.println("Press 4 to display previous order");
					System.out.println("Press 5 to re-run a previous order");
					System.out.println("Press 0 to exit");
					userInput = ur.nextLine();
					userInputToInt = Integer.parseInt(userInput);

					//item search
					if(userInputToInt == 1){

						System.out.println("Enter 1 to search by stock number, manufacturer, model number, category, or description attribute and value ");
						System.out.println("Enter 2 to search for compatible items of an item ");


						String userSearchOption = ur.nextLine();

						int intOption = Integer.parseInt(userSearchOption);

						if(intOption == 1){
							System.out.println("Enter the value for the corresponding search paramter, or simply press the Enter key to skip");
							for(int i = 0; i < searchParameters.size(); i++){
								System.out.print(searchParameters.get(i));


								String userParameter = ur.nextLine();
								holdParameters.add(userParameter);
							}
							System.out.println("The following items match your search parameters!");
							//System.out.println(holdParameters.size());
							Emart.searchEmartCatalog(holdParameters.get(0),holdParameters.get(1),holdParameters.get(2),holdParameters.get(3),holdParameters.get(4),holdParameters.get(5),stmt); 
						}

						if(intOption == 2){
							System.out.print("Enter the stock number of the item to see compatible items: ");
							String userStockno = ur.nextLine();
							Emart.searchCompatibility(userStockno, stmt);
							


						}



					

					
					}

					//shopping cart
					if(userInputToInt == 2){
						System.out.println("Enter 1 to add an item to cart");
						System.out.println("Enter 2 to remove an item from cart");
						System.out.println("Enter 3 view cart");
						System.out.print("Enter option: ");

						String cartOption = ur.nextLine();	
						int intCartOption = Integer.parseInt(cartOption);

						if(intCartOption == 1){
							System.out.print("Enter the stock nubmer of the item: ");
							String currentStockno = ur.nextLine();
							int availableQuantity = Edepot.getQuantity(currentStockno, stmt);
							if(availableQuantity <= 0){
								System.out.println(currentStockno + " not available!");
							}
							else{
								System.out.println("Available quantity: " + availableQuantity);
								System.out.print("Enter the quantity you wish to purchase: ");
								String qty = ur.nextLine();
								int intQty = Integer.parseInt(qty);


								//check edepot stock and see if the quanity is less than or equal to stock

								Emart.insertToCart(currentStockno, username, intQty,stmt);
							}


						}

						if(intCartOption == 2){
							System.out.print("Enter the stock nubmer of the item you wish to remove from your cart: ");
							String currentStockno = ur.nextLine();

							Emart.removeFromCart(currentStockno, username,stmt);

							System.out.println(currentStockno + " successfully deleted from cart!");


						}

						if(intCartOption == 3){

							Emart.viewCart(username,stmt);

						}


					}

					//checkout
					if(userInputToInt == 3){
						//get total of shopping cart

						//calculate total based on user
						userStatus = Emart.getUserStatus(username, stmt);

						float discount = Emart.getDiscount(userStatus);
						float floatDiscount = 1 - discount/100;


						float cartTotal = Emart.checkoutHelper(username,stmt);
						


						if(cartTotal == -1){
							System.out.println("Your shopping cart is empty!");
							return;
						}

						
						/* 
						if(cartTotal > 100){
							System.out.println("Shipping and handling waived!");
							System.out.println("Your status is " + userStatus + " which means you get a " + discount + "% discount");
							System.out.println("Your cart total is $" + cartTotal + " without discount applied");
							float newTotal = cartTotal*floatDiscount;
							System.out.println("Your cart total is $" + newTotal  + " with the " + discount + "% discount applied");
							
							//Emart.updatecurrentOrderno(stmt);
						}

						else{
							System.out.println("Shipping and handling fee: $100");
							System.out.println("Your status is " + userStatus + " which means you get a " + discount + "% discount");
							System.out.println("Your cart total is $" + cartTotal + " without discount applied or shipping fee applied");
							float newTotal = cartTotal*floatDiscount + 100;
							System.out.println("Your cart total is $" + newTotal + " with the " + discount + "% discount applied and shipping fee");
							//Emart.updatecurrentOrderno(stmt);
							
						}
						*/



						//update the user status, ie add it to previous order and see the previous three
						//update the status accordingly 


					}
					
					//display previous order for given order number
					if(userInputToInt == 4){
						System.out.println("Enter order number to view previous order: ");
						int userOrderno = Integer.parseInt(ur.nextLine());
						Emart.viewPreviousOrder(userOrderno, username, stmt);

					}
					
					//rerun previous order
					if(userInputToInt == 5){
						System.out.println("Enter order number to re-run a previous order: ");
						int userOrderno = Integer.parseInt(ur.nextLine());
						Emart.viewPreviousOrder(userOrderno, username, stmt);
						Emart.rerunPreviousOrder(userOrderno, username, stmt);;

					}
				}
			}
			
			//manager commands
			if(qr && managerStatus==1){
				
				System.out.println("Welcome back to eMart MANAGER " + username + "!");
				
				String userInput;
				int userInputToInt = -1;
				while(userInputToInt != 0){
					List<String> holdParameters = new ArrayList<>();
					System.out.println("Press 1 to search for items");
					System.out.println("Press 2 to view shopping cart");
					System.out.println("Press 3 to checkout");
					System.out.println("Press 4 to display previous order");
					System.out.println("Press 5 to re-run a previous order");
					System.out.println("Press 6 to view manager commands");
					System.out.println("Press 0 to exit");
					userInput = ur.nextLine();
					userInputToInt = Integer.parseInt(userInput);

					//item search
					if(userInputToInt == 1){

						System.out.println("Enter 1 to search by stock number, manufacturer, model number, category, or description attribute and value, Enter 2 to search for compatible items of an item ");

						String userSearchOption = ur.nextLine();

						int intOption = Integer.parseInt(userSearchOption);

						if(intOption == 1){
							System.out.println("Enter the value for the corresponding search paramter, or simply press the Enter key to skip");
							for(int i = 0; i < searchParameters.size(); i++){
								System.out.print(searchParameters.get(i));


								String userParameter = ur.nextLine();
								holdParameters.add(userParameter);
							}
							System.out.println("The following items match your search parameters!");
							//System.out.println(holdParameters.size());
							Emart.searchEmartCatalog(holdParameters.get(0),holdParameters.get(1),holdParameters.get(2),holdParameters.get(3),holdParameters.get(4),holdParameters.get(5),stmt); 
						}

						if(intOption == 2){
							System.out.print("Enter the stock number of the item to see compatible items: ");
							String userStockno = ur.nextLine();
							Emart.searchCompatibility(userStockno, stmt);
							


						}



					

					
					}

					//shopping cart
					if(userInputToInt == 2){
						System.out.println("Enter 1 to add an item to cart");
						System.out.println("Enter 2 to remove an item from cart");
						System.out.println("Enter 3 view cart");
						System.out.print("Enter option: ");

						String cartOption = ur.nextLine();	
						int intCartOption = Integer.parseInt(cartOption);

						if(intCartOption == 1){
							System.out.print("Enter the stock nubmer of the item: ");
							String currentStockno = ur.nextLine();
							int availableQuantity = Edepot.getQuantity(currentStockno, stmt);
							if(availableQuantity <= 0){
								System.out.println(currentStockno + " not available!");
							}
							else{
								System.out.println("Available quantity: " + availableQuantity);
								System.out.print("Enter the quantity you wish to purchase: ");
								String qty = ur.nextLine();
								int intQty = Integer.parseInt(qty);


								//check edepot stock and see if the quanity is less than or equal to stock

								Emart.insertToCart(currentStockno, username, intQty,stmt);
							}

						}

						if(intCartOption == 2){
							System.out.print("Enter the stock nubmer of the item you wish to remove from your cart: ");
							String currentStockno = ur.nextLine();

							Emart.removeFromCart(currentStockno, username,stmt);

							System.out.println(currentStockno + " successfully deleted from cart!");


						}

						if(intCartOption == 3){

							Emart.viewCart(username,stmt);

						}


					}

					//checkout
					if(userInputToInt == 3){
						//get total of shopping cart

						//calculate total based on user
						userStatus = Emart.getUserStatus(username, stmt);

						float discount = Emart.getDiscount(userStatus);
						float floatDiscount = 1 - discount/100;


						float cartTotal = Emart.checkoutHelper(username,stmt);
						


						if(cartTotal == -1){
							System.out.println("Your shopping cart is empty!");
							return;
						}

						

						if(cartTotal > 100){
							System.out.println("Shipping and handling waived!");
							System.out.println("Your status is " + userStatus + " which means you get a " + discount + "% discount");
							System.out.println("Your cart total is $" + cartTotal + " without discount applied");
							float newTotal = cartTotal*floatDiscount;
							System.out.println("Your cart total is $" + newTotal  + " with the " + discount + "% discount applied");
							
							//Emart.updatecurrentOrderno(stmt);
						}

						else{
							System.out.println("Shipping and handling fee: $100");
							System.out.println("Your status is " + userStatus + " which means you get a " + discount + "% discount");
							System.out.println("Your cart total is $" + cartTotal + " without discount applied or shipping fee applied");
							float newTotal = cartTotal*floatDiscount + 100;
							System.out.println("Your cart total is $" + newTotal + " with the " + discount + "% discount applied and shipping fee");
							//Emart.updatecurrentOrderno(stmt);
							
						}




						//update the user status, ie add it to previous order and see the previous three
						//update the status accordingly 


					}
					
					//display previous order for given order number
					if(userInputToInt == 4){
						System.out.println("Enter order number to view previous order: ");
						int userOrderno = Integer.parseInt(ur.nextLine());
						Emart.viewPreviousOrder(userOrderno, username, stmt);

					}
					
					//rerun previous order
					if(userInputToInt == 5){
						System.out.println("Enter order number to re-run a previous order: ");
						int userOrderno = Integer.parseInt(ur.nextLine());
						Emart.viewPreviousOrder(userOrderno, username, stmt);

					}
					
					//manager commands
					if(userInputToInt == 6){

						int managerOption = -1;
						while(managerOption != 0){
							System.out.println("Press 1 to print monthly summary");
							System.out.println("Press 2 to adjust customer status");
							System.out.println("Press 3 to send an order to manufacturer");
							System.out.println("Press 4 to change the price of an item");
							System.out.println("Press 5 to delete previous order,");
							System.out.println("Press 0 to exit");
							managerOption = Integer.parseInt(ur.nextLine());
							if(managerOption == 1){
								Emart.managerMonthlySummary(stmt);
							}
							if(managerOption == 2){
								System.out.print("Enter cid of customer you want to changed status of: ");
								String cid = ur.nextLine();
								System.out.print("Enter new status you want to assign: ");
								String newStatus = ur.nextLine();
								Emart.managerChangeStatus(cid, newStatus, stmt);
							}
							if(managerOption == 3){
								System.out.print("Enter manufacturer: ");
								String manufacturer = ur.nextLine();
								System.out.print("Enter stock number: ");
								String stockno = ur.nextLine();
								System.out.print("Enter quantity: ");
								int quantity = Integer.parseInt(ur.nextLine());

								Emart.managerSendOrder(manufacturer, stockno, quantity);
							}
							if(managerOption == 4){
								System.out.print("Enter stock number: ");
								String stockno = ur.nextLine();
								System.out.print("Enter new price: ");
								float newPrice = Float.parseFloat(ur.nextLine());

								Emart.managerChangePrice(stockno, newPrice, stmt);
							}
							if(managerOption == 5){
								System.out.print("Enter order number you wish to delete: ");
								int orderno = Integer.parseInt(ur.nextLine());

								Emart.deletePreviousOrder(orderno, stmt);
							}



						}


					}
				}
			}
		





		}

	}

	public static void EdepotLogin(Statement stmt) throws SQLException{

		System.out.print("Enter username: ");
		String username = ur.nextLine();

		System.out.print("Enter password: ");
		String password = ur.nextLine();

		//System.out.println(loginQuery);
		String loginQuery = "SELECT * FROM EMARTCUSTOMERS WHERE CID = '" + username + "' AND PASSWORD = '" + password + "'";
		
		ResultSet rs = stmt.executeQuery(loginQuery);

		//query success
		boolean qr = rs.next();
		int managerStatus = rs.getInt(7);

		if(qr && managerStatus == 0){
			System.out.println("Invalid manager login credentials!");
		}

		if(qr && managerStatus == 1){
			System.out.println("Welcome back MANAGER: " + username + "!");
			System.out.println("press 1 to manage shipping notices, press 0 to exit");
			String actionOption = ur.nextLine();
			String managerOption = "start";

			if(actionOption.equals("1")){
				while(!managerOption.equals("0")){
					System.out.println("press 1 to view current shipping notices");
					System.out.println("press 2 to receive a shipping notice");
					System.out.println("press 3 to receive a shipment");
					System.out.println("press 4 to view quantity of item");
					System.out.println("press 0 to go back");
					managerOption = ur.nextLine();
					if(managerOption.equals("1")){
						String sqlQuery = "SELECT * FROM EDEPOTSHIPPINGNOTICE";
						ResultSet snrs = stmt.executeQuery (sqlQuery);
						while(snrs.next()){
							System.out.println("shipnotid: " + snrs.getString(1) + ", compyname: " + snrs.getString(2) + ", manufacturer: " +  snrs.getString(3) + ", stockno: " +  snrs.getString(4) + ", modelno: " + snrs.getString(5) + ", quantity: " + snrs.getString(6) + ", received: " + snrs.getString(7) );
						}
						snrs.close();
					}
	
					if(managerOption.equals("2")){
						System.out.println("Enter shipping notice details ");
						
						System.out.print("Enter unique shipping notice id: ");
						String shipnotid = ur.nextLine();
						System.out.print("Enter stock number: ");
						String stockno = ur.nextLine();
						System.out.print("Enter company name: ");
						String companyname = ur.nextLine();
						System.out.print("Enter manufacturer: ");
						String manufacturer = ur.nextLine();
						System.out.print("Enter model number: ");
						int modelno = Integer.parseInt(ur.nextLine());
						System.out.print("Enter quantity: ");
						int quantity = Integer.parseInt(ur.nextLine());
						
						Edepot.receiveShippingNotice(shipnotid, stockno, companyname, manufacturer, modelno, quantity, stmt);
	
					}
				
					if(managerOption.equals("3")){
						System.out.println("Enter shipping notice to receive shipment:");
						String shipnotid = ur.nextLine();
						Edepot.receiveShipment(shipnotid, stmt);
					}
	
					if(managerOption.equals("4")){
						System.out.print("Enter stock number of item: ");
						String currentStockno = ur.nextLine();
	
						System.out.println("Quantity: " + Edepot.getQuantity(currentStockno, stmt));
	
	
					}

				}	
				System.out.println("exited eDepot");

			}
		}
        

    }
	

}