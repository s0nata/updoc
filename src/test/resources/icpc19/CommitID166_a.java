/**
 *  Warning: loadCartForUpdate(...) and saveUpdatedCartToOrder(...) must always
 *           be used together in this sequence.
 *           In fact loadCartForUpdate(...) will remove or cancel data associated to the order,
 *           before returning the ShoppingCart object; for this reason, the cart
 *           must be stored back using the method saveUpdatedCartToOrder(...),
 *           because that method will recreate the data.
 */
private static ShoppingCart loadCartForUpdate(LocalDispatcher dispatcher,
        GenericDelegator delegator, GenericValue userLogin, String orderId) throws GeneralException;