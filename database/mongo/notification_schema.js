db.createCollection("notifications", {
  validator: {
    $jsonSchema: {
      bsonType: "object",
      required: ["userId", "message", "status", "type", "createdAt"],
      properties: {
        userId: {
          bsonType: "string",
          description: "User ID - required"
        },
        message: {
          bsonType: "string",
          description: "Notification message - required"
        },
        status: {
          bsonType: "string",
          enum: ["unread", "read"],
          description: "Notification status - must be 'unread' or 'read'"
        },
        type: {
          bsonType: "string",
          description: "Notification type - required"
        },
        createdAt: {
          bsonType: "date",
          description: "Creation timestamp - required"
        }
      }
    }
  }
});
