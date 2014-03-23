package de.soniro.detrin.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;

import de.soniro.detrin.exception.InvalidInstanceException;

public class DatasetTest {

	private static final String ATTRIBUTE_NAME = "attribute";
	private static final String INSTANCE_VALUE = "value";
	
	Dataset dataset = new Dataset();
	
	@Mock
	Instance instance;
	
	@Mock
	Instance otherInstance;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void attributeCanBeAdded() {
        assertTrue(dataset.getAttributes().isEmpty());
        final NominalAttribute attribute = addAttributeToDataset();
		assertEquals(1, dataset.getAttributes().size());
		assertEquals(attribute, dataset.getAttributeByName(ATTRIBUTE_NAME));
	}
	
	@Test(expected = InvalidInstanceException.class)
	public void invalidInstanceExceptionIfInstanceWithoutAttributeIsAddedToDataset() throws InvalidInstanceException {
		assertTrue(dataset.getInstances().isEmpty());
		dataset.addInstance(new Instance());		
	}
	
	@Test
	public void validInstanceCanBeAddedToDataset() throws InvalidInstanceException {
		assertTrue(dataset.getInstances().isEmpty());
		dataset.addInstance(createValidInstance());
		assertEquals(1, dataset.size());
	}
	
	@Test(expected = InvalidInstanceException.class)
	public void invalidInstanceThrowsInvalidInstanceExceptionIfAdded() throws InvalidInstanceException {
		assertTrue(dataset.getInstances().isEmpty());
		dataset.addInstance(createInvalidInstance());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getAttributeByNameThrowsExceptionIfNoAttributeExists() {
		addAttributeToDataset();
		dataset.getAttributeByName("invalidAttribute");
	}
	
	@Test
	public void getSubsetForAttributeValueReturnsAllAttributesWithTheValue() throws InvalidInstanceException {
		NominalAttribute attribute = setupAttributeAndInstances();		
		Dataset subset = dataset.getSubsetForAttributeValue(attribute, INSTANCE_VALUE);
		assertEquals(2, dataset.size());
		assertEquals(1, subset.size());
	}
	
	@Test
	public void getSubsetForAttributeGroupReturnsAllAttributesContainingToTheGroup() throws InvalidInstanceException {
		NominalAttribute attribute = setupAttributeAndInstances();
		NominalGroup group = new NominalGroup(ATTRIBUTE_NAME);
		group.getValues().add(INSTANCE_VALUE);
		Dataset subset = dataset.getSubsetForAttributeGroup(attribute, group);
		assertEquals(2, dataset.size());
		assertEquals(1, subset.size());
	}
	
	@Test
	public void getSubsetForAttributeGroupReturnsAllAttributesContainingToTheNumericGroup() throws InvalidInstanceException {
		NumericAttribute attribute = addNumericAttributeToDataset();
		Interval interval = new Interval(0d, 10d);
		initInstanceMocks(attribute, 5d, 30d);		
		Dataset subset = dataset.getSubsetForAttributeGroup(attribute, interval);
		assertEquals(2, dataset.size());
		assertEquals(1, subset.size());
	}

	@Test
	public void getValueCountReturnsZeroWithoutInstances() throws InvalidInstanceException {
		NominalAttribute attribute = addAttributeToDataset();
		assertEquals(Long.valueOf(0), dataset.getValueCount(attribute, INSTANCE_VALUE));
	}
	
	@Test
	public void getValueCountCountsHowOftenTheValueOccureInTheInstances() throws InvalidInstanceException {
		NominalAttribute attribute = setupAttributeAndInstances();
		when(instance.getValueForAttribute(attribute)).thenReturn(INSTANCE_VALUE);
		when(otherInstance.getValueForAttribute(attribute)).thenReturn(INSTANCE_VALUE);
		assertEquals(Long.valueOf(2), dataset.getValueCount(attribute, INSTANCE_VALUE));
	}
	
	@Test
	public void getValueCountDoesNotCountInstancesWithOtherValues() throws InvalidInstanceException {
		NominalAttribute attribute = setupAttributeAndInstances();		
		assertEquals(Long.valueOf(1), dataset.getValueCount(attribute, INSTANCE_VALUE));
	}
	
	@Test
	public void cloneDataset() throws InvalidInstanceException {
		NominalAttribute attribute = addAttributeToDataset();
		Instance instance = new Instance();
		instance.put(attribute, INSTANCE_VALUE);
		dataset.addInstance(instance);

		Dataset clone = (Dataset) dataset.clone();
		
		assertEquals(dataset, clone);
	}

	private NominalAttribute setupAttributeAndInstances() throws InvalidInstanceException {
		NominalAttribute attribute = addAttributeToDataset();
		initInstanceMocks(attribute, INSTANCE_VALUE, "invalidValue");
		return attribute;
	}

	private <T> void initInstanceMocks(Attribute<T> attribute, T value, T otherValue)
			throws InvalidInstanceException {
		when(instance.getAttributes()).thenReturn(Sets.newSet(attribute));
		when(otherInstance.getAttributes()).thenReturn(Sets.newSet(attribute));
		dataset.addInstance(instance);
		dataset.addInstance(otherInstance);
		when(instance.getValueForAttribute(attribute)).thenReturn(value);
		when(otherInstance.getValueForAttribute(attribute)).thenReturn(otherValue);
	}
	
	private Instance createValidInstance() {
		final Instance instance = new Instance();
		final NominalAttribute attribute = addAttributeToDataset();
		instance.put(attribute, "value");
		return instance;
	}
	
	private NominalAttribute addAttributeToDataset() {
		final NominalAttribute attribute = new NominalAttribute(ATTRIBUTE_NAME);
		dataset.addAttribute(attribute);
		return attribute;
	}
	
	private NumericAttribute addNumericAttributeToDataset() {
		final NumericAttribute attribute = new NumericAttribute(ATTRIBUTE_NAME);
		dataset.addAttribute(attribute);
		return attribute;
	}
	
	private Instance createInvalidInstance() {
		final Instance instance = new Instance();
		addAttributeToDataset();
		instance.put(new NominalAttribute("nonExistingAttribute"), "value");
		return instance;
	}
	
}
