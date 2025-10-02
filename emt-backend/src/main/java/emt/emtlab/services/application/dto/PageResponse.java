// src/main/java/.../PageResponse.java (ensure correct package)
package emt.emtlab.services.application.dto; // Or your application's DTO package

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PageResponse<T> {
    private List<T> content;
    private int number;       // Current page number (0-indexed)
    private int size;         // Items per page
    private long totalElements;
    private int totalPages;
    private String sortBy;    // The sort property actually used
    private String direction; // The sort direction actually used (asc/desc)

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.number = page.getNumber();
        this.size = page.getSize(); // Crucial: Ensure this is populated
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();

        Sort sort = page.getSort();
        if (sort != null && !sort.isUnsorted()) {
            // Assuming single sort criteria for simplicity, adapt if multiple sorts are used/possible
            Sort.Order order = sort.iterator().next();
            if (order != null) {
                this.sortBy = order.getProperty();
                this.direction = order.getDirection().name().toLowerCase();
            } else {
                // Fallback if Sort object exists but has no Order (should be rare for Pageable results)
                this.sortBy = null; // Or set from original request if needed
                this.direction = null;
            }
        } else {
            // Handle unsorted or if you want to reflect the requested sort even if default
            this.sortBy = null; // Or set from original request if needed
            this.direction = null;
        }
    }

    // GETTERS for all fields are required for Jackson serialization
    public List<T> getContent() {
        return content;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getDirection() {
        return direction;
    }
}