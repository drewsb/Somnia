const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

/*
const {Client} = require('@xmpp/client')

const client = new Client()

client.start('fcm-xmpp.googleapis.com:5235');
*/

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });

exports.sendMessage = functions.https.onRequest((req, res) => {
  //const regToken = "dg2RnpzMj4k:APA91bHzL3Q_FYUREcKDOPXbJQctxPTMjpouIAC-UO8tE24bykYdIUZhDlvFfOY0KkJb2WnIZ4POPEO3ZCmZPBmZgmol4aAmvLFIOfrnq0vEt1vQzcF05gBwRPQi31gJ_XIXGig9fSrU"

  const regToken = "cCsJ2aR8PSE:APA91bH9bvqxw4qFsLsb358Z-XzxGZOhywx_LIt4enG4CkYCnN2XDGSeIAfjz3HwleXq4-2vbV2Bm4I_VcS6x6zH5qkNKNnnUNQJvyzeZtnwwppap0GaOZvpr8HPxB300de7Z5Gx2O-O"
   
  const payload = {
    data: {
      foo: "bar",
    },
  };

  return admin.messaging().sendToDevice(regToken, payload)
    .then(function(response) {
      console.log('Sent message.')
      return res.status(200).send("SENT");
    })
    .catch(function(error) {
      console.log('errer Sending message: ', error);
      throw new functions.https.HttpsError('unknown', error.message, error);
    });
});
