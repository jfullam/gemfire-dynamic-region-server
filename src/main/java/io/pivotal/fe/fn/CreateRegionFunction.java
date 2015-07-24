package io.pivotal.fe.fn;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.RegionAttributesFactoryBean;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.stereotype.Component;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionAttributes;

@Component
public class CreateRegionFunction {

	@Resource(name="metadataRegion")
	private Region<String, RegionAttributes> metadataRegion;
	@Autowired
	private Cache cache;

	public enum Status {
		SUCCESSFUL, UNSUCCESSFUL, ALREADY_EXISTS
	};

	public CreateRegionFunction() {

	}

	@GemfireFunction(id="CreateRegionFunction")
	public boolean createRegion(String regionName) {
		
		RegionAttributes attributes =  new RegionAttributesFactoryBean().create();
		
		Status status = Status.SUCCESSFUL;
		Region region = this.cache.getRegion(regionName);
		
		 if (region == null) {
			 // Put the attributes into the metadata region. The afterCreate call will actually create the region.
			 this.metadataRegion.put(regionName, attributes);
	
			 // Retrieve the region after creating it
			 region = this.cache.getRegion(regionName);
			 if (region == null) {
				 status = Status.UNSUCCESSFUL;
			 }
		 } else {
			 status = Status.ALREADY_EXISTS;
		 }
		 	return status.equals(Status.UNSUCCESSFUL) ? false : true;
	}

	public String getId() {
		System.out.println(getClass().getSimpleName());
		return getClass().getSimpleName();
	}

	public boolean optimizeForWrite() {
		return false;
	}

	public boolean isHA() {
		return true;
	}

	public boolean hasResult() {
		return true;
	}

}
