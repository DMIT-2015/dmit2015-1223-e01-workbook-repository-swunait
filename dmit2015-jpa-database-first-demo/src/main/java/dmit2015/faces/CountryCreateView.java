package dmit2015.faces;

import dmit2015.entity.Country;
import dmit2015.entity.Region;
import dmit2015.persistence.CountryRepository;
import dmit2015.persistence.RegionRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Messages;

import java.math.BigInteger;
import java.util.List;

@Named("currentCountryCreateView")
@RequestScoped
public class CountryCreateView {

    @Inject
    private CountryRepository _countryRepository;

    @Getter
    private Country newCountry = new Country();

    @Inject
    private RegionRepository _regionRepository;

    @NotNull(message = "Country region must be assigned")
    @Getter @Setter
    private BigInteger selectedRegionId;

    @Getter
    private List<Region> regionList;

    @PostConstruct  // After @Inject is complete
    public void init() {
        try {
            regionList = _regionRepository.findAll();
        } catch (Exception ex) {
            Messages.addGlobalError(ex.getMessage());
        }
    }

    public String onCreateNew() {
        String nextPage = "";
        try {
            if (selectedRegionId != null) {
                Region selectedRegion = _regionRepository.findById(selectedRegionId).orElseThrow();
                newCountry.setRegionsByRegionId(selectedRegion);
            }

            _countryRepository.add(newCountry);
            Messages.addFlashGlobalInfo("Create was successful. {0}", newCountry.getCountryId());
            nextPage = "index?faces-redirect=true";
        } catch (Exception e) {
            e.printStackTrace();
            Messages.addGlobalError("Create was not successful. {0}", e.getMessage());
        }
        return nextPage;
    }

}