package com.example.jakartaeeapp.users;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/users")
public class UsersResource {

    @Inject
    @ConfigProperty(name = "SAMPLE_KEY")
    private String sampleKey;

    @Inject
    @ConfigProperty(name = "APP_ENV", defaultValue = "production")
    private String appEnv;

    @GET
    @Path("/test-config")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> config() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Test Config");
        response.put("sampleKey", sampleKey);
        response.put("appEnv", appEnv);
        return response;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> users() {
        Map<String, Object> response = new HashMap<>();
        List<UserModel> users = new ArrayList<>();
        response.put("message", "User data");
        response.put("data", users);
        return response;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") int id) {
        if (id == 1) {
            UserModel user = new UserModel.Builder()
                    .id(0)
                    .name("Alice")
                    .age(18)
                    .build();

            // 200 OK
            return Response.status(Response.Status.OK).entity(user).build();
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User not found");

            // 404 Not Found
            return Response.status(Response.Status.NOT_FOUND).entity(error).build();
        }
    }

    @GET
    @Path("/test-auth")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAuth(@HeaderParam("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Missing or invalid token\"}")
                    .build();
        }

        String token = authHeader.substring("Bearer ".length());
        // validate token here...

        return Response.ok("{\"message\":\"Valid token: " + token + "\"}").build();
    }

    @GET
    @Path("/test-cookie")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfile(@CookieParam("session_id") String sessionId) {
        if (sessionId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"No session cookie\"}")
                    .build();
        }

        // Handle get profile data here

        return Response.ok("{\"session\":\"" + sessionId + "\"}").build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(Map<String, Object> newUser) {
        // Handles data from request body

        return Response.status(Response.Status.CREATED)  // 201
                .entity(newUser)
                .build();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormParam("username") String username, // Text field
            @FormParam("part") EntityPart filePart // File upload part
    ) {
        // return Response.ok("File uploaded").build();

        // Access the username directly
        System.out.println("User: " + username);

        // 1. Get Part Information
//        String name = filePart.getName(); // The name of the form field (e.g., 'file')
//        String fileName = filePart.getFileName().orElse("unknown_file"); // The original filename
//        MediaType mediaType = filePart.getMediaType(); // The content type of the part
//
//        System.out.println("Processing file part: " + name);
//        System.out.println("Original file name: " + fileName);
//        System.out.println("Media Type: " + mediaType);

        return Response.ok("File uploaded").build();

        // 2. Access and Save the File Content
//        try (InputStream inputStream = filePart.getContent()) {
//
//            // --- Basic File Saving Example ---
//            // NOTE: In a real application, you'd use a safer, configured path.
//            // Ensure the directory exists and the application has write permissions.
//            java.nio.file.Path targetPath = Paths.get("/tmp/", fileName);
//            Files.copy(inputStream, targetPath);
//
//            System.out.println("File saved successfully to: " + targetPath.toAbsolutePath());
//
//            return Response.ok("File uploaded and saved: " + fileName).build();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("File upload failed: " + e.getMessage())
//                    .build();
//        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") int id, UserModel updatedUser) {
        // Handle user here

        return Response.ok(updatedUser).build();
    }

    // ✅ DELETE: Remove user by ID
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") int id) {
        // Handle user here

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // Sample code when handling multiple input data
    // The order of parameters in your method signature does not matter.
    @PUT
    @Path("/test-data/{id}")
    public Response updateUser(
            @PathParam("id") int userId,             // from URL path /users/{id}
            @QueryParam("notify") @DefaultValue("false") boolean notify, // from ?notify=true
            UserModel userUpdate,                    // from request body (JSON)
            @CookieParam("session_id") String sessionId,  // from Cookie: session_id=abc
            @Context HttpHeaders headers             // full headers if needed
    ) {
        // Example log
        System.out.println("Updating user: " + userId);
        System.out.println("Notify? " + notify);
        System.out.println("New name: " + userUpdate.getName());
        System.out.println("Session cookie: " + sessionId);

        // Business logic here...
        Map<String, Object> response = new HashMap<>();
        response.put("id", userId);
        response.put("updatedName", userUpdate.getName());
        response.put("notify", notify);

        return Response.ok(response).build();
    }
}
