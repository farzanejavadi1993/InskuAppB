package ir.kitgroup.inskuappb.ui.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.google.gson.JsonElement;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ir.kitgroup.inskuappb.data.model.ContactId;
import ir.kitgroup.inskuappb.data.model.Log;
import ir.kitgroup.inskuappb.data.model.ModelCatalogPage;
import ir.kitgroup.inskuappb.data.model.ModelGift;
import ir.kitgroup.inskuappb.dataBase.ModelCatalog;
import ir.kitgroup.inskuappb.dataBase.ModelCatalogPageItem;
import ir.kitgroup.inskuappb.data.model.ModelSetting;
import ir.kitgroup.inskuappb.dataBase.Ord;
import ir.kitgroup.inskuappb.dataBase.OrdDetail;
import ir.kitgroup.inskuappb.repository.FakeCompanyRepository;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class CompanyViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private FakeCompanyRepository fakeRepository;

    private CompanyViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new CompanyViewModel(fakeRepository, null);
    }

    @Test
    public void testGetCatalogPage() {
        // Given
        List<ModelCatalogPage> fakeCatalogPageResult = new ArrayList<>();
        when(fakeRepository.getCatalogPage("user", "pass", "catalogId")).thenReturn(Observable.just(fakeCatalogPageResult));

        // When
        viewModel.getCatalogPage("user", "pass", "catalogId", null);

        // Then
        assertEquals(fakeCatalogPageResult, viewModel.getResultCatalogPage().getValue());
    }

    @Test
    public void testGetCatalogPageItemList() {
        // Given
        List<ModelCatalogPageItem> fakeCatalogPageItemListResult = new ArrayList<>();
        when(fakeRepository.getCatalogPageItemList("user", "pass", "catalogId", "pageId")).thenReturn(Observable.just(fakeCatalogPageItemListResult));

        // When
        viewModel.getCatalogPageItemList("user", "pass", "catalogId", "pageId", null);

        // Then
        assertEquals(fakeCatalogPageItemListResult, viewModel.getResultCatalogPageItemList().getValue());
    }

    @Test
    public void testGetCustomerList() {
        // Given
        JsonElement fakeCustomerListResult = null; // Provide fake result here
        when(fakeRepository.getCustomerList("user", "pass", "mobile")).thenReturn(Observable.just(fakeCustomerListResult));

        // When
        viewModel.getCustomerList("user", "pass", "mobile", null);

        // Then
        assertEquals(fakeCustomerListResult, viewModel.getResultCustomerList().getValue());
    }

    @Test
    public void testGetContactId() {
        // Given
        List<ContactId> fakeContactIdResult = new ArrayList<>();
        when(fakeRepository.getContactId("user", "pass", "mobile")).thenReturn(Observable.just(fakeContactIdResult));

        // When
        viewModel.getContactId("user", "pass", "mobile", null);

        // Then
        assertEquals(fakeContactIdResult, viewModel.getResultContactId().getValue());
    }

    @Test
    public void testGetSetting() {
        // Given
        List<ModelSetting> fakeSettingResult = new ArrayList<>();
        when(fakeRepository.getSetting("user", "pass")).thenReturn(Observable.just(fakeSettingResult));

        // When
        viewModel.getSetting("user", "pass", null);

        // Then
        assertEquals(fakeSettingResult, viewModel.getResultSetting().getValue());
    }

    @Test
    public void testGetCatalog() {
        // Given
        List<ModelCatalog> fakeCatalogResult = new ArrayList<>();
        when(fakeRepository.getCatalog("user", "pass")).thenReturn(Observable.just(fakeCatalogResult));

        // When
        viewModel.getCatalog("user", "pass", null);

        // Then
        assertEquals(fakeCatalogResult, viewModel.getResultCatalog().getValue());
    }

    @Test
    public void testGetCatalogPageItem() {
        // Given
        List<ModelCatalogPageItem> fakeCatalogPageItemResult = new ArrayList<>();
        when(fakeRepository.getCatalogPageItem("user", "pass", "itemId")).thenReturn(Observable.just(fakeCatalogPageItemResult));

        // When
        viewModel.getCatalogPageItem("user", "pass", "itemId", null);

        // Then
        assertEquals(fakeCatalogPageItemResult, viewModel.getResultCatalogPageItem().getValue());
    }

    @Test
    public void testSendOrder() {
        // Given
        List<Ord> fakeOrds = new ArrayList<>();
        List<OrdDetail> fakeOrdDetails = new ArrayList<>();
        List<ModelGift> fakeModelGifts = new ArrayList<>();
        List<Log> fakeSendOrderResult = new ArrayList<>();
        when(fakeRepository.sendOrder("user", "pass", fakeOrds, fakeOrdDetails, fakeModelGifts)).thenReturn(Observable.just(fakeSendOrderResult));

        // When
        viewModel.sendOrder("user", "pass", fakeOrds, fakeOrdDetails, fakeModelGifts, null);

        // Then
        assertEquals(fakeSendOrderResult, viewModel.getResultSendOrder().getValue());
    }

    @Test
    public void testDeleteOrder() {
        // Given
        List<Log> fakeDeleteOrderResult = new ArrayList<>();
        when(fakeRepository.deleteOrder("user", "pass", "id")).thenReturn(Observable.just(fakeDeleteOrderResult));

        // When
        viewModel.deleteOrder("user", "pass", "id", null);

        // Then
        assertEquals(fakeDeleteOrderResult, viewModel.getResultDeleteOrder().getValue());
    }

}
