package de.soniro.detrin.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
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
		setupInstances(attribute, 5d, 30d);		
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
		dataset.addInstance(createValidInstance());
		Dataset clone = (Dataset) dataset.clone();		
		assertEquals(dataset, clone);
	}
	
	@Test
	public void getCurrentValuesForAttributeReturnsAllInstanceValuesForAttribute() throws InvalidInstanceException {
		NominalAttribute attribute = setupAttributeAndInstances();
		Set<String> currentValues = dataset.getCurrentValuesForAttribute(attribute);
		assertEquals(2, currentValues.size());
		assertTrue(currentValues.contains(INSTANCE_VALUE));
		assertTrue(currentValues.contains("invalidValue"));
	}
	
	@Test
	public void getCurrentValuesForAttributeIgnoresNegativeValues() throws InvalidInstanceException {
		NumericAttribute attribute = addNumericAttributeToDataset();
		setupInstances(attribute, 5d, -30d);	
		Set<Double> currentValues = dataset.getCurrentValuesForAttribute(attribute);
		assertEquals(1, currentValues.size());
		assertTrue(currentValues.contains(5d));
		assertFalse(currentValues.contains(-30d));
	}
	
	@Test
	public void equalDatasetHaveSameHashCodeAndStringRepresentation() throws InvalidInstanceException {
		dataset.addInstance(createValidInstance());
		Dataset clone = (Dataset) dataset.clone();		
		assertTrue(dataset.equals(clone));
		assertEquals(dataset.toString(), clone.toString());
		assertEquals(dataset.hashCode(), clone.hashCode());
	}

	@Test
	public void unequalDatasetHaveDifferentHashCodeAndStringRepresentation() throws InvalidInstanceException {
		dataset.addInstance(createValidInstance());
		Dataset firstDataset = (Dataset) dataset.clone();
		NumericAttribute attribute = addNumericAttributeToDataset();
		setupInstances(attribute, 5d, 30d);
		assertFalse(dataset.equals(firstDataset));
		assertFalse(dataset.toString().equals(firstDataset.toString()));
		assertFalse(dataset.hashCode() == firstDataset.hashCode());
	}
	
	@Test
	public void getMostProperValueForAttributeReturnsMostCommonValue() throws InvalidInstanceException {
		NominalAttribute attribute = setupAttributeAndInstances();
		setupInstance(attribute, Mockito.mock(Instance.class), INSTANCE_VALUE);
		setupInstance(attribute, Mockito.mock(Instance.class), "oneValueMore");
		attribute.getPossibleValues().add("invalidValue");
		attribute.getPossibleValues().add(INSTANCE_VALUE);
		attribute.getPossibleValues().add("oneValueMore");
		String mostProperValue = dataset.getMostProperValueForAttribute(attribute);
		assertEquals(INSTANCE_VALUE, mostProperValue);
	}
	
	private NominalAttribute setupAttributeAndInstances() throws InvalidInstanceException {
		NominalAttribute attribute = addAttributeToDataset();
		setupInstances(attribute, INSTANCE_VALUE, "invalidValue");
		return attribute;
	}

	private <T> void setupInstances(Attribute<T> attribute, T value, T otherValue)
			throws InvalidInstanceException {
		setupInstance(attribute, instance, value);
		setupInstance(attribute, otherInstance, otherValue);
	}
	
	private <T> void setupInstance(Attribute<T> attribute, Instance instance, T value) throws InvalidInstanceException {
		Set<Attribute<?>> attributeSet = Sets.newSet(attribute);
		when(instance.getAttributes()).thenReturn(attributeSet);
		dataset.addInstance(instance);
		when(instance.getValueForAttribute(attribute)).thenReturn(value);
	}
	
	private Instance createValidInstance() {
		final Instance instance = new Instance();
		final NominalAttribute attribute = addAttributeToDataset();
		instance.put(attribute, INSTANCE_VALUE);
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
