//package com.psm.controller;
//
//public class ForTomorrowUse {
//
//    @Transactional
//    public ResultVO addOrder(String cids, Order order) {
//        List<CartVO> cartVOS = getCartItems(cids);
//        boolean isStockAvailable = checkStockAvailability(cartVOS);
//
//        if (isStockAvailable) {
//            String orderId = generateOrderId();
//            order.setOrderId(orderId);
//            order.setOrderStatus("To Pay");
//            order.setCreateTime(new Date());
//
//            int rowsAffected = saveOrder(order);
//
//            if (rowsAffected > 0) {
//                generateOrderItems(cartVOS, orderId);
//                updateProductStock(cartVOS);
//                deleteCarts(cids);
//
//                return new ResultVO(RespondStatus.OK, "Order made successfully", orderId);
//            } else {
//                return new ResultVO(RespondStatus.NO, "Failed to increase", null);
//            }
//        } else {
//            return new ResultVO(RespondStatus.NO, "Out of stock, fail to checkout", null);
//        }
//    }
//
//    private List<CartVO> getCartItems(String cids) {
//        String[] arr = cids.split(",");
//        List<Integer> cidsList = new ArrayList<>();
//        for (int i = 0; i < arr.length; i++) {
//            cidsList.add(Integer.parseInt(arr[i]));
//        }
//        return cartDAO.queryListCartByMultiCartId(cidsList);
//    }
//
//    private boolean checkStockAvailability(List<CartVO> cartVOS) {
//        for (CartVO cv : cartVOS) {
//            if (cv.getProductNum() > cv.getStock()) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private String generateOrderId() {
//        return UUID.randomUUID().toString().replace("-", "");
//    }
//
//    private int saveOrder(Order order) {
//        return orderDAO.insertOrder(order);
//    }
//
//    private void generateOrderItems(List<CartVO> cartVOS, String orderId) {
//        for (CartVO cv : cartVOS) {
//            String itemId = System.currentTimeMillis() + "" + (new Random().nextInt(89999) + 10000);
//            OrderItem orderItem = new OrderItem(itemId, orderId, cv.getStoreId(), cv.getProductId(), cv.getProductName(),
//                    cv.getMainImg(), cv.getSalePrice(), cv.getProductNum(), cv.getSalePrice() * cv.getProductNum(),
//                    cv.getCartTime(), new Date(), 0);
//
//            orderItemDAO.insertOrderItem(orderItem);
//        }
//    }
//
//    private void updateProductStock(List<CartVO> cartVOS) {
//        for (CartVO cv : cartVOS) {
//            int productId = cv.getProductId();
//            int newStock = cv.getStock() - cv.getProductNum();
//            productDAO.updateStockById(productId, newStock);
//        }
//    }
//
//    private void deleteCarts(String cids) {
//        String[] arr = cids.split(",");
//        List<Integer> cidsList = new ArrayList<>();
//        for (String cid : arr) {
//            cidsList.add(Integer.parseInt(cid));
//        }
//        for (int cd : cidsList) {
//            cartDAO.deleteCartByCardId(cd);
//        }
//    }
//
//}
