import {useState, useEffect} from 'react';
import {
    Box,
    Typography,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    IconButton,
    Button,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    MenuItem,
    Chip,
    CircularProgress,
    Alert
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import api from '../api/api';

const UsersManagementPage = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [openDialog, setOpenDialog] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        roles: []
    });
    const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
    const [userToDelete, setUserToDelete] = useState(null);

    useEffect(() => {
        const controller = new AbortController();
        fetchUsers(controller.signal);
        return () => controller.abort();
    }, []);

    const fetchUsers = async (signal) => {
        setLoading(true);
        setError(null);
        try {
            const response = await api.get('/admin/users', {signal});
            setUsers(response.data);
        } catch (err) {
            if (err.name !== 'AbortError') {
                setError(err.response?.data?.message || err.message);
            }
        } finally {
            setLoading(false);
        }
    };

    const handleEdit = (user) => {
        setSelectedUser(user);
        setFormData({
            username: user.username,
            email: user.email,
            roles: user.roles
        });
        setOpenDialog(true);
    };

    const handleCloseDialog = () => {
        setOpenDialog(false);
        setFormData({username: '', email: '', roles: []});
        setSelectedUser(null);
        setError(null);
    };

    const handleDeleteClick = (user) => {
        setUserToDelete(user);
        setIsDeleteDialogOpen(true);
    };

    const handleDeleteConfirm = async () => {
        try {
            await api.delete(`/admin/users/${userToDelete.id}`);
            await fetchUsers();
            setIsDeleteDialogOpen(false);
            setUserToDelete(null);
        } catch (err) {
            setError(err.response?.data?.message || err.message);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            if (selectedUser) {
                await api.put(`/admin/users/${selectedUser.id}`, formData);
            }
            await fetchUsers();
            handleCloseDialog();
        } catch (err) {
            setError(err.response?.data?.message || err.message);
        }
    };

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
                <CircularProgress/>
            </Box>
        );
    }

    return (
        <Box>
            {error && (
                <Alert severity="error" sx={{mb: 2}}>
                    {error}
                </Alert>
            )}
            <Typography variant="h4" component="h1" gutterBottom>
                Users Management
            </Typography>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Username</TableCell>
                            <TableCell>Email</TableCell>
                            <TableCell>Roles</TableCell>
                            <TableCell>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {users.map((user) => (
                            <TableRow key={user.id}>
                                <TableCell>{user.username}</TableCell>
                                <TableCell>{user.email}</TableCell>
                                <TableCell>
                                    {user.roles.map((role) => (
                                        <Chip
                                            key={role}
                                            label={role.replace('ROLE_', '')}
                                            size="small"
                                            sx={{mr: 0.5}}
                                        />
                                    ))}
                                </TableCell>
                                <TableCell>
                                    <IconButton onClick={() => handleEdit(user)}>
                                        <EditIcon/>
                                    </IconButton>
                                    <IconButton onClick={() => handleDeleteClick(user)}>
                                        <DeleteIcon/>
                                    </IconButton>
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Dialog open={openDialog} onClose={handleCloseDialog}>
                <DialogTitle>Edit User</DialogTitle>
                <form onSubmit={handleSubmit}>
                    <DialogContent>
                        <TextField
                            fullWidth
                            label="Username"
                            value={formData.username}
                            onChange={(e) => setFormData({...formData, username: e.target.value})}
                            margin="normal"
                            required
                        />
                        <TextField
                            fullWidth
                            label="Email"
                            type="email"
                            value={formData.email}
                            onChange={(e) => setFormData({...formData, email: e.target.value})}
                            margin="normal"
                            required
                        />
                        <TextField
                            select
                            fullWidth
                            label="Roles"
                            value={formData.roles}
                            onChange={(e) => setFormData({...formData, roles: e.target.value})}
                            margin="normal"
                            SelectProps={{
                                multiple: true
                            }}
                            required
                        >
                            <MenuItem value="ROLE_USER">User</MenuItem>
                            <MenuItem value="ROLE_ADMIN">Admin</MenuItem>
                        </TextField>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleCloseDialog}>Cancel</Button>
                        <Button type="submit" variant="contained" color="primary">Save</Button>
                    </DialogActions>
                </form>
            </Dialog>

            <Dialog open={isDeleteDialogOpen} onClose={() => setIsDeleteDialogOpen(false)}>
                <DialogTitle>Confirm Delete</DialogTitle>
                <DialogContent>
                    Are you sure you want to delete user {userToDelete?.username}?
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setIsDeleteDialogOpen(false)}>Cancel</Button>
                    <Button color="error" onClick={handleDeleteConfirm} variant="contained">Delete</Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default UsersManagementPage;