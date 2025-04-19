package interfaces;

import entities.Service;

import java.util.List;

public interface ServicesDAO extends GenericDAO<Service, String> {
    public List<Service> searchServices(String keyword, boolean availableOnly);
    public Service findServiceByID(int id);
    public List<Service> getAllServices();
    public List<Service> searchServicesByName(String keyword);
}
