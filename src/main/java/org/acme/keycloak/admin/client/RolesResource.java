package org.acme.keycloak.admin.client;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

@Path("/api/admin")
public class RolesResource {

    @Inject
    Keycloak keycloak;

    @GET
    @Path("/roles")
    public List<RoleRepresentation> getRoles() {
        return keycloak.realm("thanhtra").roles().list();
    }

    @GET
    @Path("/users")
    public List<UserRepresentation> getUsers() {
        updateUser("053ffb17-54ed-4cb9-a61b-8ea804745ccc", false);
        return keycloak.realm("thanhtra").users().list();
    }

    public void updateUser(String id, Boolean enabled) {
        UsersResource usersResource = keycloak.realm("thanhtra").users();
        UserRepresentation userRepresentation = usersResource.get(id).toRepresentation();
        userRepresentation.setEnabled(enabled != null && enabled);
        try {
//            if (userRepresentation.getUserProfileMetadata() == null) {
//                userRepresentation.setUserProfileMetadata(new UserProfileMetadata());
//            }
            usersResource.get(id).update(userRepresentation);
        } catch (ClientErrorException e) {
            Response response = e.getResponse();
            int status = response.getStatus();
            String message = response.readEntity(String.class);
            System.out.println("Status: " + status);
            System.out.println("Error Message: " + message);
        }

    }

}