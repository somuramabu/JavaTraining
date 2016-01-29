var async = require('async');
var request = require('request');
exports.handler = function(req, res) {
  async.parallel([
    /*
     * First external endpoint
     */
    function(callback) {
      var id = req.param('id');
      var url = "http://localhost:8080/product/product-"+id;
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        obj = JSON.parse(body);
        console.log(body);
        callback(false, obj);
      });
    },
    /*
     * Second external endpoint
     */
    function(callback) {
      var id = req.param('id');
      var url = "http://localhost:9090/price/price-"+id;
      request(url, function(err, response, body) {
        // JSON body
        if(err) { console.log(err); callback(true); return; }
        obj = JSON.parse(body);
        console.log(body);
        callback(false, obj);
      });
    },
  ],
  /*
   * Collate results
   */
  function(err, results) {
    if(err) { console.log(err); res.send(500,"Server Error"); return; }
    //res.send({prodcut:results[0], price:results[1]});
  }
  );
};

exports.batchHandler = function(req, res) {
  var prodcutDetailsArray = {products:[]};
  var prodcutsArray = [];
  var priceArray = [];
  async.waterfall([
      async.apply(getProducts, req,res,prodcutDetailsArray),
      getPriceInfoForProdcuts
  ], function (err, result) {
    if(err) { console.log(err); res.send(500,"Server Error"); return; }
    console.log("---------------------------------------");
    console.log(result);
    //res.send(result);
  });
};

function getProducts(req,res,prodcutDetailsArray,callback) {
  var id = req.param('id');
  var url = "http://localhost:8080/product?type=stationary";
  request(url, function(err, response, body) {
    console.log("Product");
      if(err)
      {
        console.log(err);
        callback(true);
        return;
      }
      var products = JSON.parse(body);
      callback(null,products,res);
  });
}
function getPriceInfoForProdcuts(products,res, callback) {
  var urls =  {products:[]};
  var priceUrlMap =  [];
  console.log(products);
  for(var i=0 ;i<products.length;i++){
    var productId = products[i].id
    var tokens = productId.split("-");
    if (tokens[1] != undefined){
      urls.products.push({"url":"http://localhost:9090/price/price-"+tokens[1],"prodcut":products[i]});
    }
  }
  console.log(urls);
  async.map(urls.products,function (url,callback){
          console.log("getPrice"+url);
          console.log(url.prodcut);
          request(url.url, function(err, response, body) {
              console.log("Price Request Happend");
              if(err) { console.log(err); callback(true); return; }
              obj = JSON.parse(body);
              console.log(body);
              callback(err,{"product":url.prodcut,"price":obj});
              //prodcutDetailsArray.products.push({"product":product,"price":obj});
          });
        },function (err, result) {
          if(err) { console.log(err); res.send(500,"Server Error"); return; }
          console.log(result);
          console.log(products);
          res.send(result);
      })
      callback(null, products);
}

function getPrice(product){
    console.log("getPrice"+productId);
    //var url = "http://localhost:9090/price/"+productId;
    var url = "http://localhost:9090/price/price-22";
    console.log(url);
    request(url, function(err, response, body) {
          console.log("Price Request Happend");
          if(err) { console.log(err); callback(true); return; }
          obj = JSON.parse(body);
          console.log(body);
          prodcutDetailsArray.products.push({"product":product,"price":obj});
    });
}
