package com.jettra.plugin.controller.autentification;

import com.jettra.plugin.entity.autentification.Department;
import com.jettra.plugin.repository.autentification.DepartmentRepository;
import com.jettra.rest.annotations.*;
import com.jettra.rest.core.Response;
import java.util.List;

@Path("/autentification/departments")
public class DepartmentController {

    @GET
    @Produces("application/json")
    public List<Department> findAll() {
        return DepartmentRepository.findAll();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response save(Department department) {
        DepartmentRepository.save(department);
        return Response.ok("Saved successfully").build();
    }

    @DELETE
    @Path("/{id}")
    @Produces("application/json")
    public Response delete(@PathParam("id") String id) {
        DepartmentRepository.delete(id);
        return Response.ok("Deleted successfully").build();
    }
}
