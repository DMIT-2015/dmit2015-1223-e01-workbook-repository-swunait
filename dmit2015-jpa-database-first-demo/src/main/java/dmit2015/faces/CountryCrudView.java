package dmit2015.faces;

import dmit2015.entity.Country;
import dmit2015.persistence.CountryRepository;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import java.io.Serializable;
import java.util.List;

@Named("currentCountryCrudView")
@ViewScoped
public class CountryCrudView implements Serializable {

    @Inject
    private CountryRepository _countryRepository;

    @Getter
    private List<Country> countryList;

    @Getter
    @Setter
    private Country selectedCountry;

    @Getter
    @Setter
    private String selectedId;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            countryList = _countryRepository.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public void onOpenNew() {
        selectedCountry = new Country();
    }

    public void onSave() {
        if (selectedId == null) {
            try {
                _countryRepository.add(selectedCountry);
                Messages.addGlobalInfo("Create was successful. {0}", selectedCountry.getCountryId());
                countryList = _countryRepository.findAll();
            } catch (RuntimeException e) {
                Messages.addGlobalError(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
            }
        } else {
            try {
                _countryRepository.update(selectedId, selectedCountry);
                Messages.addFlashGlobalInfo("Update was successful.");
                countryList = _countryRepository.findAll();
            } catch (RuntimeException e) {
                Messages.addGlobalError(e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                Messages.addGlobalError("Update was not successful.");
            }
        }

        PrimeFaces.current().executeScript("PF('manageCountryDialog').hide()");
        PrimeFaces.current().ajax().update("form:messages", "form:dt-Countrys");
    }

    public void onDelete() {
        try {
            _countryRepository.delete(selectedCountry);
            selectedCountry = null;
            Messages.addGlobalInfo("Delete was successful.");
            countryList = _countryRepository.findAll();
            PrimeFaces.current().ajax().update("form:messages", "form:dt-Countrys");
        } catch (RuntimeException e) {
            Messages.addGlobalError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Delete not successful.");
        }
    }

}