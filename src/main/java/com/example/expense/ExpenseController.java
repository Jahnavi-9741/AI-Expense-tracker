package com.example.expense;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExpenseController {

    private final ExpenseRepository expenseRepository;
    private final AIInsightService aiInsightService;
    private final PDFService pdfService;

    public ExpenseController(ExpenseRepository expenseRepository,
                             AIInsightService aiInsightService,
                             PDFService pdfService) {
        this.expenseRepository = expenseRepository;
        this.aiInsightService = aiInsightService;
        this.pdfService = pdfService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Expense> allRecords = expenseRepository.findAll();
        List<Expense> expenses = allRecords.stream()
                .filter(e -> "Expense".equalsIgnoreCase(e.getType()))
                .toList();

        double total = expenses.stream().mapToDouble(Expense::getAmount).sum();

        model.addAttribute("expenses", expenses);
        model.addAttribute("total", total);
        model.addAttribute("insight", aiInsightService.generateInsight(expenses));
        model.addAttribute("insightCards", aiInsightService.generateInsightCards(expenses));

        return "dashboard";
    }

    @GetMapping("/expenses")
    public String expensesPage(Model model) {
        List<Expense> expenses = expenseRepository.findAll().stream()
                .filter(e -> "Expense".equalsIgnoreCase(e.getType()))
                .toList();

        Expense expense = new Expense();
        expense.setDate(LocalDate.now());
        expense.setType("Expense");

        model.addAttribute("expense", expense);
        model.addAttribute("expenses", expenses);
        return "expenses";
    }

    @PostMapping("/expenses/add")
    public String addExpense(@ModelAttribute("expense") Expense expense) {
        expense.setType("Expense");
        if (expense.getDate() == null) {
            expense.setDate(LocalDate.now());
        }
        expenseRepository.save(expense);
        return "redirect:/expenses";
    }

    @GetMapping("/bills")
    public String billsPage(Model model) {
        List<Expense> bills = expenseRepository.findAll().stream()
                .filter(e -> "Bill".equalsIgnoreCase(e.getType()))
                .toList();

        Expense bill = new Expense();
        bill.setDate(LocalDate.now());
        bill.setDueDate(LocalDate.now());
        bill.setType("Bill");

        model.addAttribute("bill", bill);
        model.addAttribute("bills", bills);
        model.addAttribute("upcomingBills", aiInsightService.getUpcomingBills(bills));
        return "bills";
    }

    @PostMapping("/bills/add")
    public String addBill(@ModelAttribute("bill") Expense bill) {
        bill.setType("Bill");
        if (bill.getDate() == null) {
            bill.setDate(LocalDate.now());
        }
        expenseRepository.save(bill);
        return "redirect:/bills";
    }

    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Long id) {
        Expense record = expenseRepository.findById(id).orElse(null);

        if (record != null && "Bill".equalsIgnoreCase(record.getType())) {
            expenseRepository.deleteById(id);
            return "redirect:/bills";
        }

        expenseRepository.deleteById(id);
        return "redirect:/expenses";
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> downloadReport() throws Exception {
        byte[] pdf = pdfService.generateReport(expenseRepository.findAll());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expense-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}