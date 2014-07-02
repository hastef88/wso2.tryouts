<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="org.wso2.carbon.orderprocessor.ui.ConsumerFactory" %>
<%@ page import="org.wso2.carbon.orderprocessor.stub.CustomerServiceStub.Customer" %>
<%@ page import="org.wso2.carbon.orderprocessor.stub.CustomerServiceStub.Address" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>

<%
    String serverURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);

    Customer newCustomer = new Customer();

    newCustomer.setEmail(request.getParameter("email"));
    newCustomer.setName(request.getParameter("name"));
    newCustomer.setPhone(request.getParameter("phone"));

    Address shippingAddress = new Address();
    shippingAddress.setAddress(request.getParameter("address"));
    shippingAddress.setCity(request.getParameter("city"));
    shippingAddress.setPostalCode(request.getParameter("postalCode"));

    newCustomer.setShippingAddress(shippingAddress);

    String customerId = "";

    try {
        customerId = ConsumerFactory.RegisterCustomer(newCustomer);
    } catch (Exception e) {
        CarbonUIMessage.sendCarbonUIMessage(e.getMessage(), CarbonUIMessage.ERROR, request, e);
%>
<script type="text/javascript">
    location.href = "../admin/error.jsp";
</script>
<%
        return;
    }
%>

<div id="middle">
    <h2>Customer Registered !!</h2>

    <div id="workArea">
        <table class="styledLeft" id="moduleTable">
            <thead>
            <tr>
                <th width="15%">ID</th>
                <th width="15%">Name</th>
                <th width="15%">Email</th>
                <th width="15%">Phone</th>
                <th width="40%">Shipping Address</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td><%=customerId%></td>
                <td><%=newCustomer.getName()%></td>
                <td><%=newCustomer.getEmail()%></td>
                <td><%=newCustomer.getPhone()%></td>
                <td><%=shippingAddress.getAddress()%></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
