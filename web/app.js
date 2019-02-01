const publicVapidKey = "BN70e1q-W_Ow7tfTwkTAi0aR6oR7fVnXudPf_oBD03kw59x8l6RalEIco-UPJewJw9yWIAfNgSm8xPCUFAJNQms"

function urlBase64ToUint8Array(base64String) {
  const padding = '='.repeat((4 - base64String.length % 4) % 4);
  const base64 = (base64String + padding)
    .replace(/\-/g, '+')
    .replace(/_/g, '/');
 
  const rawData = window.atob(base64);
  const outputArray = new Uint8Array(rawData.length);
 
  for (let i = 0; i < rawData.length; ++i) {
    outputArray[i] = rawData.charCodeAt(i);
  }
  return outputArray;
}

//https://stackoverflow.com/questions/39624676/uncaught-in-promise-domexception-subscription-failed-no-active-service-work

function subscribeForPushNotification (reg) {

  console.log('REGISTERING PUSH...');
  const subscription = reg.pushManager.
    subscribe({
      userVisibleOnly: true,
      // The `urlBase64ToUint8Array()` function is the same as in
      // https://www.npmjs.com/package/web-push#using-vapid-key-for-applicationserverkey
      applicationServerKey: urlBase64ToUint8Array(publicVapidKey)
    }, function() {
      console.log('PUSH REGISTERED')

      fetch('/subscribe', {
        method: 'POST',
        body: JSON.stringify(subscription),
        headers: {
          'content-type': 'application/json'
        }
      })
    });
};

navigator.serviceWorker.register("sw.js", {scope: "/"})
    .then(
    function (reg) {
        var serviceWorker = null;
        if (reg.installing) {
            serviceWorker = reg.installing;
            // console.log('Service worker installing');
        } else if (reg.waiting) {
            serviceWorker = reg.waiting;
            // console.log('Service worker installed & waiting');
        } else if (reg.active) {
            serviceWorker = reg.active;
            // console.log('Service worker active');
        } 

        if (serviceWorker) {
            console.log("sw current state", serviceWorker.state);
            if (serviceWorker.state == "activated") {
                //If push subscription wasnt done yet have to do here
                console.log("SERVICE WORKER ALREADY ON");
                subscribeForPushNotification(reg);
            }
            serviceWorker.addEventListener("statechange", function(e) {
                console.log("sw statechange : ", e.target.state);
                if (e.target.state == "activated") {
                    // use pushManager for subscribing here.
                    console.log("SERVICE WORKER JUST ACTIVATED");
                    subscribeForPushNotification(reg);
                }
            });
        }
    },
    function (err) {
        console.error('unsuccessful registration with ', "sw.js", err);
    }
);

