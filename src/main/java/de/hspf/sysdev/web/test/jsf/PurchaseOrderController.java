package de.hspf.sysdev.web.test.jsf;

import de.hspf.sysdev.web.test.dao.PurchaseOrder;
import de.hspf.sysdev.web.test.jsf.util.JsfUtil;
import de.hspf.sysdev.web.test.jsf.util.JsfUtil.PersistAction;
import de.hspf.sysdev.web.test.beans.PurchaseOrderFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("purchaseOrderController")
@SessionScoped
public class PurchaseOrderController implements Serializable {

    @EJB
    private de.hspf.sysdev.web.test.beans.PurchaseOrderFacade ejbFacade;
    private List<PurchaseOrder> items = null;
    private PurchaseOrder selected;

    public PurchaseOrderController() {
    }

    public PurchaseOrder getSelected() {
        return selected;
    }

    public void setSelected(PurchaseOrder selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PurchaseOrderFacade getFacade() {
        return ejbFacade;
    }

    public PurchaseOrder prepareCreate() {
        selected = new PurchaseOrder();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PurchaseOrderCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PurchaseOrderUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PurchaseOrderDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PurchaseOrder> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public PurchaseOrder getPurchaseOrder(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<PurchaseOrder> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PurchaseOrder> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PurchaseOrder.class)
    public static class PurchaseOrderControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PurchaseOrderController controller = (PurchaseOrderController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "purchaseOrderController");
            return controller.getPurchaseOrder(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PurchaseOrder) {
                PurchaseOrder o = (PurchaseOrder) object;
                return getStringKey(o.getOrderNum());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PurchaseOrder.class.getName()});
                return null;
            }
        }

    }

}
