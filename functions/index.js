const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendMessage = functions.https.onRequest((req, res) => {
  // This is my emulator's token
  //const regToken = "dg2RnpzMj4k:APA91bHzL3Q_FYUREcKDOPXbJQctxPTMjpouIAC-UO8tE24bykYdIUZhDlvFfOY0KkJb2WnIZ4POPEO3ZCmZPBmZgmol4aAmvLFIOfrnq0vEt1vQzcF05gBwRPQi31gJ_XIXGig9fSrU"

  // This is my phone's token
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

exports.notifyFriends = functions.https.onCall((data, context) => {
  console.log(data);
  console.log(context);
  //const uid = context.auth.uid;
  const uid = data.id;
  console.log(uid);
  const payload = {
    data: data
  }
  const user = admin.firestore().collection('users').doc(uid);
  var getDoc = user.get()
    .then(doc => {
      if (!doc.exists) {
        console.log('user ' + uid + " doesn't exist");
      } else {
        console.log('found user ' + uid);
	    console.log(doc);
	    var friends = doc.get('friend_ids');
	    for (var i = 0; i < friends.length; i++) {
	      sendToFriend(friends[i], payload);
	    }
      }
      return uid;
    })
    .catch(err => {
      console.log('Error getting user', err);
      return uid;
    });
});

function sendToFriend(friend_id, payload) {
  const user = admin.firestore().collection('users').doc(friend_id);
  var getDoc = user.get()
    .then(doc => {
      if (!doc.exists) {
        console.log('user ' + friend_id + "doesn't exist");
        return -1;
      } else {
        var firebase_id = doc.get('firebase_id');
        console.log("Sent to: " + friend_id);
        admin.messaging().sendToDevice(firebase_id, payload);
        return 0;
      }
    })
    .catch(err => {
      console.log('Error getting user', err);
    });
}

exports.sendDirect = functions.https.onCall((data, context) => {
  const send_to = data.other_id;
  const payload = {
    data: data
  }
  sendToFriend(send_to, payload)
});
