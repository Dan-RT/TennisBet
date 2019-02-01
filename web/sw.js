self.addEventListener('install', function(event) {
  console.log("INSTALL caught in Service Worker");
  event.waitUntil(
    caches.open('v1').then(function(cache) {
      return cache.addAll([
        '/index.html',
        '/app.js',
        '/controllers/betController.js',
        '/controllers/generalController.js',
        '/controllers/listMatchController.js',
        '/controllers/matchController.js'
      ]);
    })
  );
});

self.addEventListener('fetch', function(event) {
  console.log("FETCH caught in Service Worker");
  event.respondWith(
    caches.match(event.request).then(function(resp) {
      return resp || fetch(event.request).then(function(response) {
        return caches.open('v1').then(function(cache) {
          cache.put(event.request, response.clone());
        });  
        return response;
      });
    })
  );
});

self.addEventListener('activate', function(event) {
  var cacheKeeplist = ['v2'];

  console.log("ACTIVATE caught in Service Worker");

  event.waitUntil(
    caches.keys().then(function(keyList) {
      return Promise.all(keyList.map(function(key) {
        if (cacheKeeplist.indexOf(key) === -1) {
          return caches.delete(key);
        }
      }));
    })
  );
});

self.addEventListener('subscribe', ev => {

  console.log("PUSH enabled in Service Worker");

  const data = ev.data.json();
  console.log('Got push', data);
  self.registration.showNotification(data.title, {
    body: 'Hello, World!',
    icon: ''
  });
});







