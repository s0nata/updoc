/**
 *  Warning: this method will remove all the existing reservations of the order
 *  before returning the ShoppingCart object; for this reason, the cart
 *           must be stored back using the method saveUpdatedCartToOrder(...).
 */
private static ShoppingCart loadCartForUpdate(LocalDispatcher dispatcher,
        GenericDelegator delegator, GenericValue userLogin, String orderId) throws GeneralException;