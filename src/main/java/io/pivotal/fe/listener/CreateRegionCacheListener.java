package io.pivotal.fe.listener;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.RegionAttributes;
import com.gemstone.gemfire.cache.RegionEvent;
import com.gemstone.gemfire.cache.RegionExistsException;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;

import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateRegionCacheListener extends
		CacheListenerAdapter<String, RegionAttributes>  {
	
	@Autowired
	private Cache cache;

	public CreateRegionCacheListener() {
	}

	public void afterCreate(EntryEvent<String, RegionAttributes> event) {
		createRegion(event.getKey(), event.getNewValue());
	}

	public void afterRegionCreate(RegionEvent<String, RegionAttributes> event) {
		Region<String, RegionAttributes> region = event.getRegion();
		for (Map.Entry<String, RegionAttributes> entry : region.entrySet()) {
			createRegion(entry.getKey(), entry.getValue());
		}
	}

	private void createRegion(String regionName, RegionAttributes attributes)
	 {

	 
	 try {
		 Region region = this.cache.createRegion(regionName, attributes);
	 if (this.cache.getLogger().fineEnabled()) {
		 this.cache.getLogger().fine("CreateRegionCacheListener created: " +
	 region);
	 }
	 
	 System.out.println("CreateRegionCacheListener created: " + region);
	 
	 } catch (RegionExistsException e) {/* ignore */}
	 }

	public void init(Properties p) {
	}
}
